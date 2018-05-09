package com.gevlet.coop;




/**
 * 应用启动类
 */
public class Bootstract {



    //测试用
    public static final String APP_PATH_LOCATION = "E:\\gevlet-app";

    /**
     * 启动类
     * @param args
     */
    public static void main(String[] args) {
        Gevlet gevlet = new Gevlet();
        gevlet.start(ClassLoader.getSystemClassLoader(),APP_PATH_LOCATION);
    }



}
