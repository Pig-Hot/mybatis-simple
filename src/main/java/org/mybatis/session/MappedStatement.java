package org.mybatis.session;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
public class MappedStatement {
    private String id;
    private Map<String, MappedStatement.Mapped> mappedMap;

    public Mapped mapped(String sql, Class<?>[] param, Class<?> resultType) {
        return new Mapped(sql, param, resultType);
    }

    @Data
    @AllArgsConstructor
    public class Mapped {
        private String sql;
        private Class<?>[] param;
        private Class<?> resultType;
    }
}
