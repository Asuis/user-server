package com.real.userserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author asuis
 */
@SpringBootApplication
@ComponentScan("com.real.userserver.user.mapper")
@ComponentScan("com.real.userserver.user.dao")
@EnableDiscoveryClient
@EnableTransactionManagement
public class UserServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServerApplication.class, args);
	}
}
