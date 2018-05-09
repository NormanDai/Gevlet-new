package com.gevlet.coop.executor;

import java.util.concurrent.Executor;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-09 17:19
 */
public class ServerThreadPoolExecutor {

    private static final int threadNum = 32;

    private static final int coreNum = 8;

    private static final  int queueNum = 0;

    private static Executor executor = new CachedThreadPool().getExecutor(coreNum,threadNum,queueNum);

    public static Executor getExecutor(){
        return executor;
    }

}
