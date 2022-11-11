package com.pvictorcr.bolaodacopa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BolaodacopaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BolaodacopaApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BolaodacopaApplication.class);
	}
}
