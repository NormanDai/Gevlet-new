package com.gevlet.coop.executor;

import java.util.concurrent.Executor;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-09 17:10
 */
public interface ThreadPool {
    Executor getExecutor(int coreNum,int threadNum, int queueNum);
}
