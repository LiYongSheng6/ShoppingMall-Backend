package com.shoppingmall.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

@Configuration
@EnableAsync
public class CompletableExecutor {
    //核心线程数
    private static final int CORE_POOL_SIZE = 10;
    //最大线程数
    private static final int MAX_POOL_SIZE = 20;
    //队列大小
    private static final int QUEUE_CAPACITY = 2000;
    //线程池中的线程的名称前缀
    private static final String NAME_PREFIX = "async-service-";

    private static final String TASK_EXECUTOR = "taskExecutor-";
    /**
     * 创建线程池
     */
    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        //配置最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        //配置队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(NAME_PREFIX);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 线程池任务分发，无返回值
     */
    public CompletableFuture<Void> myRunAsync(Runnable runnable){
        return CompletableFuture.runAsync(runnable,asyncServiceExecutor());
    }

    /**
     * 线程池任务分发，有返回值
     */
    public<T> CompletableFuture<T> mySupplyAsync(Supplier<T> supplier){
        return CompletableFuture.supplyAsync(supplier,asyncServiceExecutor());
    }
}
