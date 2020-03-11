package org.mybatis.session;

import lombok.Data;
import org.mybatis.domain.DataSource;
import org.mybatis.handle.DefaultParamterHandler;
import org.mybatis.handle.ParameterHandler;
import org.mybatis.plugins.InterceptorChain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Configuration {
    private boolean cache;
    private String mapperPackageName;
    private DataSource dataSource;
    private Map<String, MappedStatement> mappedStatement = new HashMap<>();
    private List<String> pluginPackageNames;
    private InterceptorChain interceptorChain = new InterceptorChain();

    public ParameterHandler newParameterHandler(MappedStatement.Mapped mapped) {
        ParameterHandler parameterHandler = new DefaultParamterHandler(mapped);
        parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }
}
