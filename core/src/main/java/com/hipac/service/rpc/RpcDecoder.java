package com.hipac.service.rpc;

import com.hipac.service.common.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huqiang on 15/10/31.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private static final Logger logger= LoggerFactory.getLogger(RpcDecoder.class);

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass){
        this.genericClass=genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.info("[RpcDecoder - decode ] ");
        if (byteBuf.readableBytes()<4){
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength=byteBuf.readInt();
        if(dataLength<0){
            channelHandlerContext.close();
        }
        if (internalBuffer().readableBytes()<dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data=new byte[dataLength];
        byteBuf.readBytes(data);
        Object obj= SerializationUtil.deserialize(data,genericClass);
        list.add(obj);
    }
}
