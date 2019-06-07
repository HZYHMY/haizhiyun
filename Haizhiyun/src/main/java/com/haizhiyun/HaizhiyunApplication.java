package com.haizhiyun;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("com.haizhiyun.dao")
public class HaizhiyunApplication {
	
	private static Logger logger = LoggerFactory.getLogger(HaizhiyunApplication.class);

	public static void main(String[] args) {
		logger.info("----------------------------------------");
		SpringApplication.run(HaizhiyunApplication.class, args);
		logger.info("----------------------------------------");
	}

}
