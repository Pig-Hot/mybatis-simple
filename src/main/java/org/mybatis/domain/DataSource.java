package org.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataSource {
    private String driver;
    private String url;
    private String username;
    private String password;
}
