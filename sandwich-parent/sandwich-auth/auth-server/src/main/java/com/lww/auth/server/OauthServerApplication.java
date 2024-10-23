package com.lww.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class OauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }

    /*
    * 1.获取 token
    * post http://localhost:9080/oauth/token?grant_type=password&redirect_uri=http://www.baidu.com&client_id=c1&client_secret=secret&username=zhangsan&password=123
    * 2. 请求头 携带 auth    Beaber token
    *
    *
    * */
}

