package org.mybatis.session.inter;

import java.util.List;

public interface Session {

    <T> T selectOne(String statement, Object parameter);

    <T> List<T> selectList(String statement, Object parameter);

    <T> T getMapper(Class<T> type);

}
