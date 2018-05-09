package com.gevlet.coop.connector;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Name:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 17:48
 */
public abstract class AbstractHandlerAdapter extends ChannelInboundHandlerAdapter {


    protected abstract void doRequest(ChannelHandlerContext context, Object message) throws Exception;

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception {
        doRequest(context,  message);
    }

}
