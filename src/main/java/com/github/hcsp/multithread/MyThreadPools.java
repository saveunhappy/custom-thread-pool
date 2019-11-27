package com.github.hcsp.multithread;

import java.util.concurrent.*;

public class MyThreadPools {
    private static final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.DiscardPolicy();
    private static final BlockingQueue<Runnable> myWorkQueue = new LinkedBlockingQueue<>(20);

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 10; // 池中允许的最大线程数目
    private static final String THREAD_NAME = "MyThread";

    // 创建一个线程池，满足以下要求：
    // 同时最多有10个任务在执行
    // 任务等待队列大小为20，如果超过20继续往该线程池中提交任务，这些任务会被悄悄丢弃
    // 线程的名字为"MyThread"
    public static ExecutorService myThreadPool() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                0, TimeUnit.SECONDS,
                myWorkQueue,
                new MyThreadFactory(),
                defaultHandler
        );
    }

    /**自定义的线程生成方式*/
    private static class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, THREAD_NAME);
        }
    }
}
