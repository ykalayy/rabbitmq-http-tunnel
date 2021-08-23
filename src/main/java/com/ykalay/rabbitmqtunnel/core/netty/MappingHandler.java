package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.core.HttpAmqpControllerModelStore;
import com.ykalay.rabbitmqtunnel.core.IncomingRequestExecutor;
import com.ykalay.rabbitmqtunnel.core.IncomingRequestWorker;
import com.ykalay.rabbitmqtunnel.http.TunnelHttpRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

@ChannelHandler.Sharable
public class MappingHandler extends BaseNettyHandler<HttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(MappingHandler.class.getName());

    private final HttpAmqpControllerModelStore httpAmqpControllerModelStore;

    private final IncomingRequestExecutor incomingRequestExecutor;

    public MappingHandler() {
        // Set the autoRelease as false, We will manage the ReferenceCount release as manually
        super(false);
        this.httpAmqpControllerModelStore = HttpAmqpControllerModelStore.getInstance();
        this.incomingRequestExecutor = IncomingRequestExecutor.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
        FullHttpRequest req = (FullHttpRequest) httpRequest;
        this.httpAmqpControllerModelStore.getHttpAmqpControllerModelList().forEach( httpAmqpControllerModel -> {
            Pattern pattern = httpAmqpControllerModel.getPattern();
            if(httpAmqpControllerModel.getPattern() != null) {
                if(pattern.matcher(req.uri()).find() &&
                        httpRequest.method().equals(HttpMethod.valueOf(httpAmqpControllerModel.getAmqpTunnelRequestMapper().httpMethod().toString()))) {

                    this.incomingRequestExecutor.submitWork(new IncomingRequestWorker(new TunnelHttpRequest(req), httpAmqpControllerModel, ctx.channel(), httpRequest));
                } else {
                    BaseNettyHandler.sendResponseWithNoBody(ctx.channel(), HttpResponseStatus.NOT_FOUND);
                }
            }
        });
    }
}
