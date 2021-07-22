package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPools {
// 创建一个线程池，满足以下要求：
// 同时最多有10个任务在执行
// 任务等待队列大小为20，如果超过20继续往该线程池中提交任务，这些任务会被悄悄丢弃
// 线程的名字为"MyThread"
public static ExecutorService myThreadPool() {
    int corePoolSize = 10;
    int maximumPoolSize = 10;
    long keepAliveTime = 0;
    TimeUnit timeUnit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>(20);
    ThreadFactory threadFactory = runnable -> new Thread(runnable, "MyThread");
    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
    return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
            blockingQueue, threadFactory, handler);
}
}
