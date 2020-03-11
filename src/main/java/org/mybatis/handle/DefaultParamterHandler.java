package org.mybatis.handle;

import org.mybatis.session.MappedStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultParamterHandler implements ParameterHandler {

    private MappedStatement.Mapped mapped;

    public DefaultParamterHandler(MappedStatement.Mapped mapped) {
        this.mapped = mapped;
    }

    @Override
    public PreparedStatement parametersize(PreparedStatement preparedStatement, Object parameter)  throws SQLException{
        Class<?> aClass = parameter.getClass();
        if (aClass.isArray()) {
            Object[] objects = (Object[]) parameter;
            if(mapped.getParam().length != objects.length){
                throw new RuntimeException("size error");
            }
            for(int i=0;i<objects.length;i++){
                setParam(preparedStatement,i+1,objects[i]);
            }
        } else {
            setParam(preparedStatement,1,parameter);
        }
        return preparedStatement;
    }

    private void setParam(PreparedStatement preparedStatement,int index,Object parameter)  throws SQLException{
        if (parameter instanceof Integer) {
            preparedStatement.setInt(index, (int) parameter);
        } else if (parameter instanceof Long) {
            preparedStatement.setLong(index, (Long) parameter);
        } else if (parameter instanceof String) {
            preparedStatement.setString(index, (String) parameter);
        }
    }

}
