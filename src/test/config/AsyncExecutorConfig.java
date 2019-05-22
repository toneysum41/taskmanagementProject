package com.tastmanager.test.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncExecutorConfig {
	 private static Logger logger = LogManager.getLogger(ExecutorConfig.class.getName());
    @Bean
    public Executor AsyncExecutor() {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	
    	executor.setCorePoolSize(10);
    	executor.setMaxPoolSize(50);
    	executor.setQueueCapacity(5000);
    	executor.setThreadNamePrefix("Athread-");
    	executor.setKeepAliveSeconds(60);
    	executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    	executor.initialize();   	
    	
    	return executor;
    }
}
