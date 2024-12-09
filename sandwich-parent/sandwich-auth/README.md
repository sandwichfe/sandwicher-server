# sandwich

## 授权码模式
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

3.根据jwt 调 resource-server中的资源接口
**请求头**带有   
**Authorization**  
**Bearer** {2步骤请求的token}




## 密码模式
oauth2.1 已经不支持密码模式 需要自定义扩展

post http://oauth.local.server:9000/oauth2/token

form-data:    

   param     | Value
   -------- | -----
   grant_type  | password
   client_id  | client_password
   client_secret  | 123456
   username  | lww
   password  | 123456


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


授权码模式： 适合两个系统完全没有关系的使用 例如我的系统 要去 通过 QQ 登录 ，那么就需要QQ授权给我 然后通过授权码以及token能拿到登录QQ用户的信息。

密码模式： 我的子系统A  访问子系统A要token   就直接拿着用户密码 去总系统 那里换取token  从而根据token访问子系统A

在我的项目这里，没有做分布式微服务，所以每个子系统都加了resource-server依赖 去认证token
如果是微服务 架构 只需要在 网关鉴权那里  做一次resource-server 就好了。



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








