package com.gevlet.coop.connector;

import com.gevlet.coop.Utils.Strings;
import com.gevlet.coop.connector.protocol.ByteBufferReader;
import com.gevlet.coop.connector.protocol.NetMessagetProtocol;
import com.gevlet.coop.core.ServerRequestHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.HashMap;


/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 17:52
 */
public class HttpRequestHandlerAdapter extends AbstractHandlerAdapter {


    private NetMessagetProtocol protocol;

    private ByteBufferReader bufferReader;


    @Override
    protected void doRequest(ChannelHandlerContext context, Object message) throws Exception {
        //处理请求头
        buildHttpRequest(message);
        //处理请求参数
        buildHttpContentAndProcess(context, message);
    }

    /**
     * 处理请求头
     *
     * @param message
     */
    private void buildHttpRequest(Object message) {
        if (message instanceof HttpRequest) {
            initialize();
            HttpRequest request = (HttpRequest) message;
            buildUrl(request.uri());
            if (HttpUtil.isContentLengthSet(request)) {
                bufferReader = new ByteBufferReader(
                        (int) HttpUtil.getContentLength(request));
            }
        }
    }

    /**
     * 处理请求参数
     *
     * @param context
     * @param message
     */
    private void buildHttpContentAndProcess(ChannelHandlerContext context, Object message) {
        if (message instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) message;
            ByteBuf content = httpContent.content();
            if (content instanceof EmptyByteBuf) {
                //处理业务请求
                ServerRequestHolder.newInstance().holdRequest(protocol);
                destroy();
                return;
            } else {

                bufferReader.reading(content);
                content.release();
                if (bufferReader.isEnd()) {
                    HashMap<String, String> contentData = getContentData(content);
                    protocol.setBody(contentData);
                    protocol.setHandlerContext(context);
                    //处理业务请求
                    ServerRequestHolder.newInstance().holdRequest(protocol);
                    //销毁请求参数
                    destroy();
                }
            }

        }
    }

    private void destroy() {
        protocol = null;
        bufferReader = null;
    }

    private HashMap<String, String> getContentData(ByteBuf content) {
        HashMap<String, String> dataMap = new HashMap<>();
        String resultStr = new String(bufferReader.readFull());
        if (!Strings.isEmpty(resultStr)) {
            String[] splitedResult = resultStr.split("\n");
            int roudNum = splitedResult.length / 4;
            for (int i = 0; i < roudNum; i++) {
                String key = splitedResult[i * 4 + 1].
                        replace("\r", "").
                        replace("Content-Disposition: form-data; name=\"", "");
                String keyResult = key.substring(0, key.length() - 1);
                String value = splitedResult[i * 4 + 3].replace("\r", "");
                //设置值
                dataMap.put(keyResult, value);
            }
        }
        return dataMap;
    }


    private void buildUrl(String url) {
        if (Strings.isEmpty(url)) {
            protocol.setThrowable(new NullPointerException("URL 不能为空"));
        }
        if ("/".equals(url)) {
            protocol.setApplication("/");
        } else {
            String[] splitedUrl = url.split("/");
            protocol.setApplication(splitedUrl[1]);
            protocol.setPathUrl(splitedUrl[2]);
            protocol.setHandlerUrl(splitedUrl[3]);
            protocol.setProtocolType("HTTP");
        }
    }


    private void initialize() {
        this.protocol = new NetMessagetProtocol();
        this.bufferReader = null;
    }
}
