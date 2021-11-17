package com.mkkl.tasks;

import java.util.concurrent.*;

public class TaskManager {
    ExecutorService pool;
    public TaskManager(int threadcount) {
        pool = Executors.newFixedThreadPool(threadcount);
    }
    public TaskManager() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public <T> Future<T> add(Callable<T> task) {
        return pool.submit(task);
    }
    public void add(Runnable task) {
        pool.submit(task);
    }

    public void close() {
        pool.shutdownNow();
    }

    public void awaitTerminationAfterShutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.MINUTES)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
