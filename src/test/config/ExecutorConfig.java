package com.tastmanager.test.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {
    private static Logger logger = LogManager.getLogger(ExecutorConfig.class.getName());
    
    @Bean
    public ThreadPoolTaskExecutor syncExecutor() {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	
    	executor.setCorePoolSize(10);
    	executor.setMaxPoolSize(50);
    	executor.setQueueCapacity(500);
    	executor.setThreadNamePrefix("thread-");
    	executor.setKeepAliveSeconds(60);
    	executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    	executor.initialize();   	
    	
    	return executor;
    }
    

}
