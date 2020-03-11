package org.mybatis.handle;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {

    PreparedStatement parametersize(PreparedStatement preparedStatement, Object parameter) throws SQLException;

}
