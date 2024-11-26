package com.lww.oauthclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Hello world!
 *
 * @author lww
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OauthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthClientApplication.class, args);
    }

    /*
    * 1.获取 token
    * post http://localhost:9080/oauth/token?grant_type=password&redirect_uri=http://www.baidu.com&client_id=c1&client_secret=secret&username=zhangsan&password=123
    * 2. 请求头 携带 auth    Beaber token
    *
    *
    * */
}

