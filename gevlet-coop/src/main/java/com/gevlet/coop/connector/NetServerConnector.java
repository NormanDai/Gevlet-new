package com.gevlet.coop.connector;

import com.gevlet.coop.exceptions.NetException;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 15:46
 */
public class NetServerConnector {


    public NetServerConnector(String protocol, int port ){
        if("HTTP".equals(protocol)){
            newHttpBackendServer(port);
        }
    }


    private void newHttpBackendServer(int port){
        BackendServer httpBackendServer = new HttpBackendServer();
        try {
            httpBackendServer.start(port);
        }catch (Throwable ex){
            throw new NetException("启动http服务异常",ex);
        }
    }

    public static void main(String[] args) {
        NetServerConnector connector = new NetServerConnector("HTTP",1208);
    }

}
