package com.naver.landsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LandsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LandsearchApplication.class, args);
	}

}
