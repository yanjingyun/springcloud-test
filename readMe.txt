
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