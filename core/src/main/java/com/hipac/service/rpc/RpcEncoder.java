package com.hipac.service.rpc;

import com.hipac.service.common.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huqiang on 15/10/31.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private static final Logger logger= LoggerFactory.getLogger(RpcEncoder.class);

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass){
        this.genericClass=genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        logger.info("[RpcEncoder - encode ] ");
        if (genericClass.isInstance(o)){
            byte[] data= SerializationUtil.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
