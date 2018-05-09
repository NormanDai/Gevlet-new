package com.gevlet.coop.executor;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-09 17:14
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private final String threadName;

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("Thread pool is EXHAUSTED! Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d), Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s), in %s://%s:%d!", new Object[]{this.threadName, Integer.valueOf(e.getPoolSize()), Integer.valueOf(e.getActiveCount()), Integer.valueOf(e.getCorePoolSize()), Integer.valueOf(e.getMaximumPoolSize()), Integer.valueOf(e.getLargestPoolSize()), Long.valueOf(e.getTaskCount()), Long.valueOf(e.getCompletedTaskCount()), Boolean.valueOf(e.isShutdown()), Boolean.valueOf(e.isTerminated()), Boolean.valueOf(e.isTerminating())});
        throw new RejectedExecutionException(msg);
    }
}
