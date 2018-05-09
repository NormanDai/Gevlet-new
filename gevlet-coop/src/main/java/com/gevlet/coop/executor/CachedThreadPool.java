package com.gevlet.coop.executor;

import java.util.AbstractQueue;
import java.util.concurrent.*;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-09 17:11
 */
public class CachedThreadPool implements ThreadPool {

    @Override
    public Executor getExecutor(int coreNum, int threadNum, int queueNum) {

        return new ThreadPoolExecutor(
                coreNum,
                threadNum,
                1000L,
                TimeUnit.MILLISECONDS,
                (BlockingQueue) (queueNum == 0 ? new SynchronousQueue() : (queueNum < 0 ? new LinkedBlockingQueue() : new LinkedBlockingQueue(queueNum))),
                new NamedThreadFactory("Gevlet server threadpool", true),
                new AbortPolicyWithReport("Gevlet server threadpool")
        );

    }
}
