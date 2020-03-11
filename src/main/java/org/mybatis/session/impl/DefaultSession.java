package org.mybatis.session.impl;

import lombok.Data;
import org.mybatis.executor.Executor;
import org.mybatis.executor.SimpleExecutor;
import org.mybatis.executor.SimpleExecutor2;
import org.mybatis.proxy.MapperProxy;
import org.mybatis.session.Configuration;
import org.mybatis.session.MappedStatement;
import org.mybatis.session.inter.Session;

import java.lang.reflect.Proxy;
import java.util.List;

@Data
public class DefaultSession implements Session {

    private Configuration configuration;

    private Executor executor;

    DefaultSession(Configuration configuration, String executorType) {
        this.configuration = configuration;
        switch (executorType) {
            case "Simple":
                executor = new SimpleExecutor(configuration);
                break;
            case "Simple2":
                executor = new SimpleExecutor2();
                break;
        }
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> selectList = this.selectList(statement, parameter);
        if (selectList == null || selectList.size() == 0) {
            return null;
        }
        if (selectList.size() == 1) {
            return selectList.get(0);
        } else {
            throw new RuntimeException("too many result");
        }
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        String[] clazzAndMethod = statement.split("\\.");
        String clazz = null;
        String method = null;
        for(int i=0;i<clazzAndMethod.length;i++){
            if(i == clazzAndMethod.length - 1){
                method = clazzAndMethod[i];
            }else {
                if(i==0){
                    clazz = clazzAndMethod[i];
                }else {
                    clazz = clazz + "." + clazzAndMethod[i];
                }
            }
        }
        MappedStatement ms = configuration.getMappedStatement().get(clazz);
        MappedStatement.Mapped msm = ms.getMappedMap().get(method);
        return executor.query(msm,parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(),new Class[]{type},new MapperProxy(this));
    }



}
