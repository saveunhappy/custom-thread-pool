package com.github.hcsp.multithread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPools {
    // 创建一个线程池，满足以下要求：
    // 同时最多有10个任务在执行
    // 任务等待队列大小为20，如果超过20继续往该线程池中提交任务，这些任务会被悄悄丢弃
    // 线程的名字为"MyThread"
    public static ExecutorService myThreadPool() {
        BlockingQueue<Runnable> queue = new MyBlockingQueue<>(20);

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, queue, r -> {
            Thread thread = new Thread(r);
            thread.setName("MyThread");
            return thread;
        });

        return executorService;
    }

    static class MyBlockingQueue<E> extends ArrayBlockingQueue<E> {
        MyBlockingQueue(int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(E e) {
            super.offer(e);
            return true;
        }
    }

}
