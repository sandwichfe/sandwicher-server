# sandwich


1. 获取授权码code  
http://oauth.local.server:9000/oauth2/authorize?client_id=client_test&redirect_uri=http://www.baidu.com&scope=read&response_type=code
2. 请求token  
post http://oauth.local.server:9000/oauth2/token  
form-data:  
  
   param     | Value
   -------- | -----
   grant_type  | authorization_code
   code  | 授权码code
   client_id  | client_test
   client_secret  | 123456
   redirect_uri  | http://www.baidu.com



登出接口：  
http://localhost:8080/logout
http://localhost:8081/logout





个人的一些理解


授权服务：myClient   带有了用户登录认证


客户端(系统A)：  系统A可以自己有一套登录系统，也可以授权登录，  向授权服务myClient  请求授权  就能拿到myClient里面的用户信息，以及带了token认证
如果系统A 系统B 都将myClient来认证登录，自己的登录系统就不做了， 登录认证去以及授权服务 myClient 来 控制  是否就是实现了单点登录？
目前就是有一个在授权服务authServer那里注册了一个客户端MyClient  
系统A用MyClient名义去请求授权登录   系统B也用MyClient名义去请求授权登录  这样子就实现了单点的登入和登出  
笑哭 现在是这样实现的  我感觉是不是有更好的方式呢。


各个组件概念：  
**1.授权服务(auth-server)：**
用于做自己的登录 （也可以理解为统一登录认证）
授权   
颁发token  


**2.资源认证服务(resource-server)：**  
可以来解析token  
资源的保护



**3.客户端应用(client)：**
不做自己的登录  用授权服务来登录以及获取token  （授权开启了jwt 可从中获取用户信息） 以及实现单点登录








