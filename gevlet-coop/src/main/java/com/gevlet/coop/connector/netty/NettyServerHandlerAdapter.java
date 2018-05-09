package com.gevlet.coop.connector.netty;

import com.gevlet.coop.Utils.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 15:59
 */
public class NettyServerHandlerAdapter extends ChannelInboundHandlerAdapter {


    private HttpRequest request;
    private ByteBufToBytes reader;
    private String url;

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        System.out.println("NettyServerHandlerAdapter.channelInactive" + context);
        super.channelInactive(context);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            url = request.uri();
            if (HttpUtil.isContentLengthSet(request)) {
                reader = new ByteBufToBytes(
                        (int) HttpUtil.getContentLength(request));
            }
        }
        if (msg instanceof HttpContent) {
            System.out.println("URL:"+url);
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();
            if (reader.isEnd()) {
                System.out.println("xxxxxxx"+content.toString(io.netty.util.CharsetUtil.UTF_8));
                String resultStr = new String(reader.readFull());
                System.out.println("Client said:" + resultStr);

                if(!Strings.isEmpty(resultStr)){
                    // \n
                    String[] splitedResult = resultStr.split("\n");
                    int roudNum = splitedResult.length / 4;
                    for (int i = 0; i < roudNum ; i++) {
                        String key = splitedResult[i * 2 + 1].
                                replace("\r","").
                                replace("Content-Disposition: form-data; name=\"","");
                        String keyResult = key.substring(0, key.length());
                        String value = splitedResult[i * 2 + 3].replace("\r","");
                    }
                }




                FullHttpResponse response = new DefaultFullHttpResponse(
                        HTTP_1_1, OK, Unpooled.wrappedBuffer("{\"tradeChannel\":\"APP\",\"merchantNo\":\"3178000008997749\",\"outTradeNo\":\"20180329011000000151000010\",\"tradeAmt\":147,\"ccy\":\"156\",\"requestDate\":\"2018-03-29 13:59:15\",\"timeOut\":0,\"operator\":\"18192021087\",\"storeCode\":\"12345\",\"storeName\":\"BBQ\",\"terminalNo\":\"201706090110000000000000123\",\"mediumType\":\"QRCODE\",\"mediumNo\":\"18192021087\",\"goodsInfo\":\"电饭锅\",\"buyerLoginNo\":\"18192047258\",\"barcode\":\"3178000000875322\"}"
                        .getBytes()));
                response.headers().set(CONTENT_TYPE, "text/json");
                response.headers().set(CONTENT_LENGTH,
                        response.content().readableBytes());
                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                context.write(response);
                context.flush();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        System.out.println("NettyServerHandlerAdapter.channelReadComplete" + context);
        super.channelReadComplete(context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        System.out.println("NettyServerHandlerAdapter.exceptionCaught" + context);
        super.exceptionCaught(context, cause);
    }
}
