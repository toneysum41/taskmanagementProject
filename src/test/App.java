package com.tastmanager.test;

import javax.servlet.MultipartConfigElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/23/2018
 * @Modify 
 * 
*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,MongoAutoConfiguration.class })
@EnableAutoConfiguration
@EnableScheduling
public class App extends SpringBootServletInitializer {
    public static void main(String[] args)
    {  	
    	Logger logger = LoggerFactory.getLogger(App.class);
        SpringApplication.run(App.class, args);
        logger.info("运行完成！！！！！！！！！！！！！");
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	return application.sources(App.class);
    }
    
    @SuppressWarnings("deprecation")
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory config = new MultipartConfigFactory();
        config.setMaxFileSize("1000MB");
        config.setMaxRequestSize("1000MB");
        return config.createMultipartConfig();
    }
}
