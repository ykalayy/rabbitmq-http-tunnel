# Rabbitmq-Http Tunnel: Asynchronous Java Http tunnel to Rabbitmq

![example workflow](https://github.com/ykalay/rabbitmq-http-tunnel/actions/workflows/build_and_deploy.yml/badge.svg) [Last-runs](https://github.com/ykalay/rabbitmq-http-tunnel/actions/workflows/build_and_deploy.yml)

Rabbitmq Http Tunnel is a Java library for asynchronous and event-based protocol switcher from HTTP to AMQP.

It allows you to use HTTP protocol as asynchronously with AMQP protocol 

#### Version 1.x
- Easy to implement your event-driven architecture with AMQP protocol over HTTP
- Async Http execution with powerful [Netty](https://github.com/netty/netty)  library  
- Powerful Rabbitmq Channel Pooling with [Apache Commons Pool](https://commons.apache.org/proper/commons-pool) for more checkout (com.ykalay.rabbitmqtunnel.rabbitmq.pool.RabbitmqChannelStore)
- No additional dependencies (just only rabbitmq-http-tunnel-1.0-jar)
- Netty Epoll support [Netty-Native Transports](https://netty.io/wiki/native-transports.html)
- Ready to use Thread-Model, you don't need to care about the thread-modeling

## Getting started
```maven
<dependency>
  <groupId>com.ykalay</groupId>
  <artifactId>rabbitmq-http-tunnel</artifactId>
  <version>1.0</version>
</dependency>
```
Run your server
```java
        RabbitMqHttpTunnelServerBuilder builder = new RabbitMqHttpTunnelServerBuilder();
        RabbitmqHttpTunnelServer server = builder.setHttpAmqpTunnelControllers(restController) // implements HttpAmqpTunnelController
                .setNettyNativeSupport(false)
                .setServerPort(8080)
                .setTimeoutSec(50)
                .setServiceName("helloWord") // Service name
                .setRabbitmqConnection(connection) // Your Rabbitmq-server connection instance
                .build();
        server.start(); // And let start the powerful server
```
Example Rest-Http Controller with Jackson
```java
public class RestController implements HttpAmqpTunnelController {

    @AmqpTunnelRequestMapper(httpMethod = HttpMethod.GET, path = "/hello")
    public AmqpMessage<ObjectNode> helloWorld(TunnelHttpRequest httpRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode testModel = objectMapper.readValue(httpRequest.getBody(), ObjectNode.class);
        AmqpMessage<ObjectNode> request = new AmqpMessage<ObjectNode>("hello", "service1-exchange", testModel);
        return request;
    }
}
```
Above controller will send your incoming HTTP Rest request into targetExchange with "hello" routing-key that's "service1-exchange" in example and the routing-key is hello
And It will start to listen the queue service queue. In example it is "helloWord-responseQueue"