
服务的注册与发现(eureka server && eureka client)
服务消费者(rest+ribbon 或 feign)
断路器(hystrix)
路由网关(zuul)
分布式配置中心(Spring Cloud Config)
高可用的分布式配置中心(Spring Cloud Config)
消息总线(Spring Cloud Bus)
路由网关(Spring Cloud Gateway)





spring-cloud-test：
	--项目参考教程：https://github.com/forezp/SpringCloudLearning
	测试服务注册中心、服务提供者、服务消费者、断路器、路由转发、配置中心、消息总线、网关

1.服务注册中心与服务提供者(eureka)：
	创建项目spring-cloud-test，并创建两个子模块：注册中心(eureka-server)、服务提供者(service-provider)
	注册中心(eureka server):
		1）引入依赖:spring-cloud-starter-eureka-server
		2）启动类添加@EnableEurekaServer注解，表明是个注册中心
		3）配置文件(application.yml)
			默认情况下eureka Server也是eureka Client，通过registerWithEureka、fetchRegistry都为false表明自己是一个eureka Server。
		4）启动
			浏览器输入：http://localhost:1001 这个时候是没有服务被发现的。
	服务提供者(eureka client):
		1）引入依赖:spring-cloud-starter-eureka
		2）启动类添加@EnableEurekaClient注解，表明自己是一个服务提供者
		3）配置文件(application.yml) --配置端口号、服务名称、服务中心地址等信息
		4）启动
			刷新eureka server项目，看到有一个已注册的服务，服务名为service-provider，端口为2001
	测试：
		启动注册中心，输入：http://localhost:1001/
		启动服务提供者，输入：http://localhost:2001/hi?name=testAA 再次回到注册中心页，能看到有服务注册上去
	服务提供者集群：
		启动eureka-server项目；
		启动service-provider项目，端口为2001
		将service-provider项目端口号改成2002后启动，能在eureka-server中看到2个服务被注册，这相当于一个小的集群。

2.服务消费者(ribbbon/feign)：
	1、service-consumer-ribbon	//ribbon+restTemplate版服务消费者
	测试：
		启动注册中心，启动两次服务提供者（端口号不同，形成集群），最后启动服务消费者
		输入：http://localhost:3001/hi?name=testAA 能看到下面内容交替出现，说明两个服务已做了负载均衡，访问了不同端口号的服务实例。
			hello testAA，I am from port：2002
			hello testAA，I am from port：2002
	2、service-consumer-feign	//feign版服务消费者
	测试：
		启动注册中心，启动两次服务提供者（端口号不同，形成集群），最后启动服务消费者
		输入：http://localhost:3002/hi?name=testAA 能看到下面内容交替出现，说明两个服务已做了负载均衡，访问了不同端口号的服务实例。
			hello testAA，I am from port：2002
			hello testAA，I am from port：2002
	3、此时架构
		1个服务注册中心eureka-server，端口号为1001
		2个服务提供者service-provider，端口号为2001和2002，都向服务注册中心注册
		2个服务消费者service-consumer-ribbon和service-consumer-feign，端口号为3001和3002，向服务中心注册。
		服务消费者通过restTemplate调用服务提供者的hi接口时，因为ribbon进行了负载均衡，会轮流调用两个服务提供者的接口。

3.断路器(hystrix)
	测试1：改造service-consumer-ribbon成为service-consumer-ribbon-hystrix
	测试2：改造service-consumer-feign成为service-consumer-feign-hystrix
		1）在application.properties中添加配置
			feign.hystrix.enabled=true
		2）创建fallback类，继承HelloRemote并实现回调方法
		3）@FeignClient中添加fallback属性
			@FeignClient(name= "SPRING-CLOUD-PRODUCER", fallback = HelloRemoteFallBack.class)

	输入：http://localhost:3004/hi?name=testAA
		testAA sorry error!	--可能会出现
		hello testAA，I am from port：2001
		hello testAA，I am from port：2002

4.路由转发和过滤器(zuul)
	路由转发：比如输入/api/user转发到到user服务,/api/shop转发到shop服务。
	测试：
		分别启动：注册中心、启动2次端口不同的服务提供者、服务消费者service-consumer-ribbon、服务消费者service-consumer-feign、路由转发service-zuul
		分别输入：
			http://localhost:4001/api-a/hi?name=testAA #该请求转发给service-consumer-ribbon服务
			http://localhost:4001/api-b/hi?name=testAA #该请求转发给service-consumer-feign服务
	测试过滤器：
		分别输入：
			http://localhost:4001/api-a/hi?name=testAA #该请求转发给service-consumer-ribbon服务
			http://localhost:4001/api-b/hi?name=testAA #请求被拦截


分布式配置中心(config)	--未测试
	描述：分布式系统的服务数量巨多，为了将配置文件统一管理，实时更新，需要配置中心组件。
	--相关依赖：
	spring-cloud-config-server	#服务端
	spring-cloud-starter-config #客户端
	描述：config-client从config-server 中获取 foo 的属性值，而config-server是从git仓库读取。
消息总线(bus)
	配置中心修改了某个属性值，此时客户端会发送一个消息，由消息总线向其他服务传递，从而使整个微服务集群都达到更新配置文件。



网关(gateway)
	Spring Cloud Gateway是官方推出的第二代网关框架，取代了zuul网关。
	网关常见功能：路由转发、权限校验、限流控制等作用。
	作用：
		协议转换，路由转发
		流量聚合，对流量进行监控，日志输出
		作为整个系统的前端工程，对流量进行控制，有限流的作用
		作为系统的前端边界，外部流量只能通过网关才能访问系统
		可以在网关层做权限的判断
		可以在网关层做缓存




********************************************************************
spring-cloud-test01 //测试eureka的注册中心和服务注册功能
	先启动eureka-server，再启动service-provider01，能看到在注册中心已经注册 service-provider01 服务
	注册中心：http://localhost:1001/
	服务提供方：http://localhost:2001/hi?name=testAA


spring-cloud-test02 //测试服务调用
	项目基于spring-cloud-test01
	ribbon版：新增service-consumer-ribbon项目
		启动注册中心eureka-server
		启动服务提供者：service-provider01，修改端口后再次启动，做一个集群。
		启动服务消费者：service-consumer-ribbon
		发送http://localhost:3001/hi?name=testAA，能看到会循环调用200x的请求

	feign版：新增service-consumer-feign项目
		启动注册中心eureka-server
		启动服务提供者：service-provider01，修改端口后再次启动，做一个集群。
		启动服务消费者：service-consumer-feign
		发送http://localhost:3002/hi?name=testAA，能看到会循环调用200x的请求


spring-cloud-test03 //测试断路器hystrix
	项目基于spring-cloud-test02改造
	ribbon版：改造service-consumer-ribbon为service-consumer-ribbon-hystrix项目
		需要引入hystrix包
		发送http://localhost:3001/hi?name=testAA，当200x节点都宕机后，会调用fallback函数

	feign版：改造service-consumer-feign为service-consumer-feign-hystrix项目
		自带hystrix，需要再application.yml开启
		发送http://localhost:3002/hi?name=testAA，当200x节点都宕机后，会调用fallback函数


spring-cloud-test04 //测试网关zuul
	项目基于spring-cloud-test03改造，新增service-zuul项目
		ribbon版：http://localhost:4001/api-a/hi?name=testAA
		feign版：http://localhost:4001/api-b/hi?name=testAA
	自定义ZuulFilter：
		http://localhost:4001/api-b/hi?name=testAA --校验不通过
		http://localhost:4001/api-b/hi?name=testAA&token=222 --校验通过



spring-cloud-test05 //测试配置中心(confug server)
	ConfigServer端：
		远程仓库https://github.com/yanjingyun/SpringCloudConfig/ 中有个文件config-client-dev.properties文件
			foo = foo version 3
		测试：http://localhost:8888/foo/dev
	ConfigClient端：
		测试：http://localhost:8881/hi，输出内容：foo version 3


spring-cloud-test06 //高可用的分布式配置中心
	项目基于spring-cloud-test05改造，新建eureka服务端，ConfigClient通过服务发现ConfigServer
	测试：http://localhost:8881/hi，输出内容：foo version 3


spring-cloud-test06-v2	//配置中心（native版本）
	描述：
		将ConfigClient中的端口号，注册中心等信息放在ConfigServer中，方便统一进行管理。
		直接将配置文件放在ConfigServer中，然后ConfigClient得到配置文件并指定环境（如dev、prod等）
	测试：
		--先后启动：EurekaServer、ConfigServer、ConfigClient三个项目
		http://localhost:2001/config-client/dev --测试配置中心的配置文件访问（访问shared下有个config-client-dev.yml文件）

		http://localhost:3001/foo	--ConfigClient读取config-client-*.yml文件的foo属性
		http://localhost:3001/user 	--读取user属性

		http://localhost:3002/foo	--ConfigClient2读取config-client2-*.yml文件的foo属性
		http://localhost:3002/user 	--读取user属性



spring-cloud-test07 //消息总线Spring Cloud Bus
	https://www.fangzhipeng.com/springcloud/2018/08/08/sc-f8-bus.html --未完成
	依次启动eureka-server、confg-cserver，启动两个config-client，端口为：8881、8882
	访问http://localhost:8881/hi 或者http://localhost:8882/hi 浏览器显示：foo version 3
	
	这时我们去代码仓库将foo的值改为“foo version 4”，即改变配置文件foo的值。如果是传统的做法，需要重启服务，才能达到配置文件的更新。此时，我们只需要发送post请求：http://localhost:8881/actuator/bus-refresh，你会发现config-client会重新读取配置文件
	这时我们再访问http://localhost:8881/hi 或者http://localhost:8882/hi 浏览器显示：foo version 4

	另外，/actuator/bus-refresh接口可以指定服务，即使用”destination”参数，比如 “/actuator/bus-refresh?destination=customers:**” 即刷新服务名为customers的所有服务。



spring-cloud-test10 //测试网关gateway
	Spring Cloud Gateway的Predict（断言）、Filter（过滤器）介绍。
	用户的请求首先经过service-gateway，根据路径由gateway的predict去断言进到哪一个router，router经过各种过滤器处理后，最后路由到具体的业务服务，比如 service-hi。

	依次启动eureka-server、service-provider01、service-provider01、service-gateway项目，service-hi为服务名
	测试：http://localhost:8082/demo/hi?name=1323  #会自动路由到service-hi服务的/hi路径中
		spring.cloud.gateway.discovery.locator.enabled为true时可访问http://localhost:8082/service-hi/hi?name=1323


spring-cloud-test10-v2 //测试网关gateway降级
    --步骤：
    1.gaeway引入hystrix包
    2.application.yml配置hystrix信息
    3.新增降级处理类
    4.service项目新建controller层方法（睡眠5秒）
    5.测试：http://localhost:8082/demo/timeout #会调用降级处理类的处理方法
spring-cloud-test10-v3 //测试网关gateway限流
    --步骤：
    1.引入redis、限流包
    2.新建限流配置类
    3.添加限流配置信息(application.yml)
    4.测试：多次刷新http://localhost:8082/demo/hi，总有几次会报429错误


