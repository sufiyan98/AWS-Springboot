package com.aws.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S3ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3ExampleApplication.class, args);
//		System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
		System.err.println("Application is running...");
	}

}
