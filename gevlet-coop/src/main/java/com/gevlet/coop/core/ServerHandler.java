package com.gevlet.coop.core;

import com.gevlet.coop.connector.protocol.NetMessagetProtocol;

/**
 * Name:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 20:40
 */
public interface ServerHandler {

    void start(ClassLoader classLoader);

    void handleRequest(NetMessagetProtocol message);


}
