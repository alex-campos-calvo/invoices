package com.invoices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InvoicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicesApplication.class, args);
	}

}
