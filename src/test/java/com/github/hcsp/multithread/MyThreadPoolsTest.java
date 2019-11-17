package com.github.hcsp.multithread;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyThreadPoolsTest {
    private ExecutorService myThreadPool = MyThreadPools.myThreadPool();

    @AfterEach
    public void cleanUp() {
        myThreadPool.shutdown();
    }

    @Test
    public void maxParallelismIsTen() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        IntStream.range(0, 11).mapToObj(i -> createTask(counter)).forEach(myThreadPool::submit);

        Thread.sleep(500);
        Assertions.assertEquals(10, counter.get());
    }

    @Test
    public void maxTaskQueueIs20() throws ExecutionException, InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        List<Future<Void>> futures =
                IntStream.range(0, 50)
                        .mapToObj(i -> createTask(counter))
                        .map(myThreadPool::submit)
                        .collect(Collectors.toList());

        for (Future<Void> future : futures) {
            future.get();
            if (counter.get() == 30) {
                break;
            }
        }

        Assertions.assertEquals(30, counter.get());
    }

    @Test
    public void threadsHaveProperNames() throws Exception {
        AtomicReference<String> name = new AtomicReference<>();
        myThreadPool.submit(() -> name.set(Thread.currentThread().getName())).get();

        Assertions.assertEquals("MyThread", name.get());
    }

    private Callable<Void> createTask(AtomicInteger counter) {
        return () -> {
            counter.incrementAndGet();
            Thread.sleep(1000);
            return null;
        };
    }
}
