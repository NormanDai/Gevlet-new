package com.gevlet.coop.connector.protocol;

import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 18:27
 */
public class NetMessagetProtocol {

    private String protocolType;

    private String application;

    private String pathUrl;

    private String handlerUrl;

    private Object body;

    private Object response;

    private Throwable throwable;

    private ChannelHandlerContext handlerContext;


    public NetMessagetProtocol() {
       super();
    }

    public NetMessagetProtocol(String application, String pathUrl, String handlerUrl, Object response) {
        this.application = application;
        this.pathUrl = pathUrl;
        this.handlerUrl = handlerUrl;
        this.response = response;
    }

    public NetMessagetProtocol(String application, String pathUrl, String handlerUrl) {
        this.application = application;
        this.pathUrl = pathUrl;
        this.handlerUrl = handlerUrl;
    }

    public NetMessagetProtocol(String application, String pathUrl, String handlerUrl, Object response, Throwable throwable) {
        this.application = application;
        this.pathUrl = pathUrl;
        this.handlerUrl = handlerUrl;
        this.response = response;
        this.throwable = throwable;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getHandlerUrl() {
        return handlerUrl;
    }

    public void setHandlerUrl(String handlerUrl) {
        this.handlerUrl = handlerUrl;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public ChannelHandlerContext getHandlerContext() {
        return handlerContext;
    }


    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public void setHandlerContext(ChannelHandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }


    @Override
    public String toString() {
        return "NetMessagetProtocol{" +
                "protocolType='" + protocolType + '\'' +
                ", application='" + application + '\'' +
                ", pathUrl='" + pathUrl + '\'' +
                ", handlerUrl='" + handlerUrl + '\'' +
                ", body=" + body +
                ", response=" + response +
                ", throwable=" + throwable +
                ", handlerContext=" + handlerContext +
                '}';
    }
}
