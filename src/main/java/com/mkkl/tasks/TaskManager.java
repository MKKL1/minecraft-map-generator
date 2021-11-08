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

    public void close() {
        pool.shutdownNow();
    }
}
