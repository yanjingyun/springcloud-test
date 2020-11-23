spring cloud + spring security + oauth2 集成：

参考教程：
    https://blog.csdn.net/zhenghongcs/article/details/107241168


测试：
    依次启动EurekaServerApplication、OauthServerApplication、GatewayServerApplication、ServiceApiApplication
    测试参考《spring-cloud-oauth.postman_collection.json》

服务列表如下：
eureka-server：注册中心

oauth-server：
    使用keytool生成RSA证书jwt.jks，复制到resource目录下，在JDK的bin目录下使用如下命令即可：
        keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
    UserServiceImpl 实现Spring Security的UserDetailsService接口，用于加载用户信息
    Oauth2ServerConfig 认证服务相关配置Oauth2ServerConfig，需要配置加载用户信息的服务UserServiceImpl及RSA的钥匙对KeyPair
    JwtTokenEnhancer 想往JWT中添加自定义信息的话，比如说登录用户的ID，可以自己实现TokenEnhancer接口
    KeyPairController 获取RSA公钥接口，还需要配置Spring Security，允许获取公钥接口的访问
    ResourceServiceImpl 初始化的时候把资源与角色匹配关系缓存到Redis中，方便网关服务进行鉴权的时候获取

gateway-server：网关
    ResourceServerConfig 资源服务器配置
    AuthorizationManager 鉴权管理器，用于判断是否有资源的访问权限
    AuthGlobalFilter 将登录用户的JWT转化成用户信息的全局过滤器

service-api： 普通服务
    搭建一个API服务，它不会集成和实现任何安全相关逻辑，全靠网关来保护它。
    创建一个测试接口，网关验证通过即可访问。
    创建一个LoginUserHolder组件，用于从请求的Header中直接获取登录用户信息
    创建一个获取当前用户信息的接口






