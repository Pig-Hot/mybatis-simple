package org.mybatis.executor;

import org.mybatis.handle.ParameterHandler;
import org.mybatis.reflection.Reflection;
import org.mybatis.session.Configuration;
import org.mybatis.session.MappedStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    private Connection connection;

    private Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
        try {
            Class.forName(configuration.getDataSource().getDriver());
            connection = DriverManager.getConnection(configuration.getDataSource().getUrl()
                    , configuration.getDataSource().getUsername(), configuration.getDataSource().getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public <E> List<E> query(MappedStatement.Mapped msm, Object parameter) {
        List<E> ret = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String regex = "#\\{([^}])*\\}";
        String  sql = msm.getSql().replaceAll(regex,"?");
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ParameterHandler parameterHandler = configuration.newParameterHandler(msm);
        try {
            parameterHandler.parametersize(preparedStatement, parameter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resultSet = preparedStatement.executeQuery();
            handlerResultSet(resultSet, ret,msm.getResultType().getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private <E> void handlerResultSet(ResultSet resultSet, List<E> ret,String className){
        Class<E> clazz = null;
        //通过反射获取类对象
        try {
            clazz = (Class<E>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while (resultSet.next()) {
                Object entity = clazz.newInstance();
                //通过反射工具 将 resultset 中的数据填充到 entity 中
                Reflection.setPropToBeanFromResultSet(entity, resultSet);
                ret.add((E) entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
