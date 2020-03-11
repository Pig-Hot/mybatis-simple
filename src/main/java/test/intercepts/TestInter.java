package test.intercepts;

import org.mybatis.handle.ParameterHandler;
import org.mybatis.plugins.*;

import java.sql.PreparedStatement;

@Intercepts({@Signature(type = ParameterHandler.class, method = "parametersize", args = {PreparedStatement.class, Object.class})})
public class TestInter implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("in");
        System.out.println(invocation.getTarget());
        System.out.println("out");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
