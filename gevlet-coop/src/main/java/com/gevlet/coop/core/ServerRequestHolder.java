package com.gevlet.coop.core;

import com.gevlet.coop.connector.protocol.NetMessagetProtocol;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 20:49
 */
public class ServerRequestHolder {

    private static volatile ServerRequestHolder serverRequestHolder;

    private ConcurrentHashMap<String, ServerHandler> serverHandlers = new ConcurrentHashMap<>();


    private ServerRequestHolder() {
    }

    public synchronized static ServerRequestHolder newInstance() {
        if (null == serverRequestHolder) {
            serverRequestHolder = new ServerRequestHolder();
        }
        return serverRequestHolder;
    }

    public void holdRequest(NetMessagetProtocol message) {

        if (null != message) {
            if ("HTTP".equals(message.getProtocolType())) {
                //处理请求
                ServerHandler serverHandler = ServerContainer.getServerContainer().getServerHandler(message.getApplication());
                serverHandler.handleRequest(message);
            } else if ("TCP/IP".equals(message.getProtocolType())) {
                // UN-DO
            } else if ("JMX".equals(message.getProtocolType())) {
                //UN-DO
            }
        }
    }


//    /**
//     * 测试方法
//     *
//     * @param message
//     */
//    private void testHoldRequest(NetMessagetProtocol message) {
//        if (null != message) {
//            if ("HTTP".equals(message.getProtocolType())) {
//                ServerHandler serverHandler = HttpRestServerHandler.getServer("");
//                serverHandler.handleRequest(message);
//            }
//            message.getHandlerContext().write(getResponse());
//            message.getHandlerContext().flush();
//        }
//    }
//
//    /**
//     * 测试用方法
//     *
//     * @return
//     */
//    private FullHttpResponse getResponse() {
//        FullHttpResponse response = new DefaultFullHttpResponse(
//                HTTP_1_1, OK, Unpooled.wrappedBuffer("{\"tradeChannel\":\"APP\",\"merchantNo\":\"3178000008997749\",\"outTradeNo\":\"20180329011000000151000010\",\"tradeAmt\":147,\"ccy\":\"156\",\"requestDate\":\"2018-03-29 13:59:15\",\"timeOut\":0,\"operator\":\"18192021087\",\"storeCode\":\"12345\",\"storeName\":\"BBQ\",\"terminalNo\":\"201706090110000000000000123\",\"mediumType\":\"QRCODE\",\"mediumNo\":\"18192021087\",\"goodsInfo\":\"电饭锅\",\"buyerLoginNo\":\"18192047258\",\"barcode\":\"3178000000875322\"}"
//                .getBytes()));
//        response.headers().set(CONTENT_TYPE, "text/json");
//        response.headers().set(CONTENT_LENGTH,
//                response.content().readableBytes());
//        response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        return response;
//    }

}
