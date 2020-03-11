package test;

import org.mybatis.session.impl.DefaultSessionFactory;
import org.mybatis.session.inter.Session;
import org.mybatis.session.inter.SessionFactory;
import test.dao.UserMapper;
import test.pojo.User;

public class App {
    public static void main(String[] args) {
        SessionFactory factory = new DefaultSessionFactory();
        Session session = factory.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.queryUserById(1);
        System.out.println(user.getPartyName());
    }
}
