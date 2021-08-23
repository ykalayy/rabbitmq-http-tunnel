package com.ykalay.rabbitmqtunnel.core.netty;

import com.ykalay.rabbitmqtunnel.core.HttpAmqpControllerModelStore;
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

    private final IncomingRequestWorker incomingRequestWorker;

    public MappingHandler() {
        // Set the autoRelease as false, We will manage the ReferenceCount release as manually
        super(false);
        this.httpAmqpControllerModelStore = HttpAmqpControllerModelStore.getInstance();
        this.incomingRequestWorker = IncomingRequestWorker.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpRequest req = (FullHttpRequest) httpRequest;
        this.httpAmqpControllerModelStore.getHttpAmqpControllerModelList().forEach( httpAmqpControllerModel -> {
            Pattern pattern = httpAmqpControllerModel.getPattern();
            if(httpAmqpControllerModel.getPattern() != null) {
                if(pattern.matcher(req.uri()).find() &&
                        httpRequest.method().equals(HttpMethod.valueOf(httpAmqpControllerModel.getAmqpTunnelRequestMapper().httpMethod().toString()))) {
                        this.incomingRequestWorker.submitWork(() -> {
                            try {
                                httpAmqpControllerModel.getControllerMethod().invoke(new TunnelHttpRequest(req));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                } else {
                    BaseNettyHandler.sendResponseWithNoBody(channelHandlerContext.channel(), HttpResponseStatus.NOT_FOUND);
                }
            }
        });
    }
}
