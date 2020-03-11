package org.mybatis.session.impl;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mybatis.anno.Select;
import org.mybatis.domain.DataSource;
import org.mybatis.plugins.Interceptor;
import org.mybatis.reflection.ClazzUtil;
import org.mybatis.session.Configuration;
import org.mybatis.session.MappedStatement;
import org.mybatis.session.inter.Session;
import org.mybatis.session.inter.SessionFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;


public class DefaultSessionFactory implements SessionFactory {

    private static Configuration configuration = new Configuration();

    private String executorType;

    private final static String DEFAULT_PATH = "config.xml";

    private void loadXMLMapperInfo(InputStream inputStream) {
        SAXReader reader = new SAXReader();
        Document document = null;
        String driver = null;
        String url = null;
        String username = null;
        String password = null;
        String cache = null;
        String mapperPackageName = null;
        String executor = null;
        List<String> pluginList = new ArrayList<>();
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (document != null) {
            Element e = document.getRootElement();
            Element datasource = e.element("environment").element("datasource");
            if (datasource != null) {
                driver = datasource.element("driver").attributeValue("value");
                url = datasource.element("url").attributeValue("value");
                username = datasource.element("username").attributeValue("value");
                password = datasource.element("password").attributeValue("value");
            }
            cache = e.element("cache").attributeValue("value");
            mapperPackageName = e.element("package").attributeValue("value");
            Element plugins = e.element("plugins");
            Iterator iterator = plugins.elementIterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                String value = element.attributeValue("value");
                pluginList.add(value);
            }
            executor = e.element("executor").attributeValue("value");

        }
        configuration.setDataSource(new DataSource(driver, url, username, password));
        configuration.setCache(Boolean.valueOf(cache));
        configuration.setMapperPackageName(mapperPackageName);
        configuration.setPluginPackageNames(pluginList);
        executorType = executor;
    }

    @Override
    public Session openSession() {
        return openSession(DEFAULT_PATH);
    }

    @Override
    public Session openSession(String path) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return openSession(inputStream);
    }

    @Override
    public Session openSession(InputStream inputStream) {
        loadXMLMapperInfo(inputStream);
        parse(configuration);
        return new DefaultSession(configuration, executorType);
    }

    private void parse(Configuration configuration) {
        parseMapper(configuration);
        parseIntercepts(configuration);
    }

    private void parseIntercepts(Configuration configuration) {
        List<String> pluginPackageNames = configuration.getPluginPackageNames();
        for (String clazz : pluginPackageNames) {
            try {
                Object o = Class.forName(clazz).newInstance();
                if (o instanceof Interceptor) {
                    configuration.getInterceptorChain().addInterceptor((Interceptor) o);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseMapper(Configuration configuration) {
        String scanPath = configuration.getMapperPackageName();
        List<Class<?>> classes = ClazzUtil.getClasses(scanPath);
        for (Class<?> aClass : classes) {
            MappedStatement mappedStatement = new MappedStatement();
            Map<String, MappedStatement.Mapped> mappedMap = new HashMap<>();
            String id = aClass.getName();
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<?> resultType = method.getReturnType();
                Select annotation = method.getAnnotation(Select.class);
                String sql = annotation.sql();
                MappedStatement.Mapped mapped = mappedStatement.mapped(sql, parameterTypes, resultType);
                mappedMap.put(methodName, mapped);
            }
            mappedStatement.setId(id);
            mappedStatement.setMappedMap(mappedMap);
            configuration.getMappedStatement().put(id, mappedStatement);
        }
    }
}
