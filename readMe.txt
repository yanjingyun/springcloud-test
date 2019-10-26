spring-cloud-test：
	测试服务注册中心、服务提供者、服务消费者
1.服务注册中心与服务提供者(eureka)：
	创建项目spring-cloud-test1，并创建两个子模块：注册中心(eureka-server)、服务提供者(service-provider)
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



