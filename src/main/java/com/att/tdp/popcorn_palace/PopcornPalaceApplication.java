package com.att.tdp.popcorn_palace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.att.tdp.popcorn_palace")
@EntityScan("com.att.tdp.popcorn_palace.model")
@EnableJpaRepositories("com.att.tdp.popcorn_palace.repository")
public class PopcornPalaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopcornPalaceApplication.class, args);
	}

}
