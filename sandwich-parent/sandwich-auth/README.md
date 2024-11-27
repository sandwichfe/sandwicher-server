# sandwich
个人的一些理解


授权服务：myClient   带有了用户登录认证


客户端(系统A)：  系统A可以自己有一套登录系统，也可以授权登录，  向授权服务myClient  请求授权  就能拿到myClient里面的用户信息，以及带了token认证
如果系统A 系统B 都将myClient来认证登录，自己的登录系统就不做了， 登录认证去以及授权服务 myClient 来 控制  是否就是实现了单点登录？






