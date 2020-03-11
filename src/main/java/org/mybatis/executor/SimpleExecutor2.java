package org.mybatis.executor;

import org.mybatis.session.MappedStatement;

import java.util.List;

public class SimpleExecutor2 implements Executor {
    @Override
    public <E> List<E> query(MappedStatement.Mapped msm, Object parameter) {
        return null;
    }
}
