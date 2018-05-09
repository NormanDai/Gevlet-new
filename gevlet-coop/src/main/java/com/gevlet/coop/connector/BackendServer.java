package com.gevlet.coop.connector;

import io.netty.channel.socket.SocketChannel;

/**
 * Name:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 17:44
 */
public interface BackendServer {

    void start(int port) throws Exception;

    void setSocketChannel(SocketChannel channel);
}
