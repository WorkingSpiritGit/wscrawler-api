package com.example.crawlertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrawlertestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrawlertestApplication.class, args);
	}

}
