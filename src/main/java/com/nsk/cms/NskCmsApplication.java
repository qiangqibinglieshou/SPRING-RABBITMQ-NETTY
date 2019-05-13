package com.nsk.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NskCmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NskCmsApplication.class, args);
	}

}
