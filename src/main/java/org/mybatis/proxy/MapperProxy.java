package org.mybatis.proxy;

import org.mybatis.session.inter.Session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

public class MapperProxy implements InvocationHandler {

    private Session session;

    public MapperProxy(Session session) {
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  {
        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            return session.selectList(method.getDeclaringClass().getName() + "." + method.getName(), args);
        } else {
            return session.selectOne(method.getDeclaringClass().getName() + "." + method.getName(), args);
        }
    }
}
