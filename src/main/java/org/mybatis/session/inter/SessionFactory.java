package org.mybatis.session.inter;

import java.io.InputStream;

public interface SessionFactory {
    Session openSession();
    Session openSession(String path);
    Session openSession(InputStream inputStream);
}
