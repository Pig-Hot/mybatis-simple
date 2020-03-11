package org.mybatis.executor;

import org.mybatis.session.MappedStatement;

import java.util.List;

public interface Executor {
    <E> List<E> query(MappedStatement.Mapped msm, Object parameter);
}
