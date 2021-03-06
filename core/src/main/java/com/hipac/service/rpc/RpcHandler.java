package com.hipac.service.rpc;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Map;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huqiang on 15/10/31.
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger= LoggerFactory.getLogger(RpcHandler.class);
    private final Map<String,Object> handlerMap;

    public RpcHandler(Map<String,Object> handlerMap){
        this.handlerMap=handlerMap;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        logger.info("[RpcHandler - channelRead0 ] {}  ",rpcRequest);
        RpcResponse response=new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        try {
            response.setResult(handler(rpcRequest));
        }catch (Exception e){
            logger.error("[RpcHandler - channelRead0 fail ]",e);
            response.setError(e);
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handler(RpcRequest request) throws Exception {
        String className=request.getClassName();
        Object serviceBean=handlerMap.get(className);
        if(serviceBean==null){
            throw new RuntimeException("not found this class: "+className);
        }
        Class<?> serviceClass=serviceBean.getClass();
        String methodName=request.getMethodName();
        Class<?>[] parameterTypes=request.getParameterTypes();
        Object[] parameters=request.getParameters();
        FastClass fastClass=FastClass.create(serviceClass);
        FastMethod fastMethod=fastClass.getMethod(methodName,parameterTypes);
        return fastMethod.invoke(serviceBean,parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("[RpcHandler - exceptionCaught fail ]",cause);
        ctx.close();
    }
}
