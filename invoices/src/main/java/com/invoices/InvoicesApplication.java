package com.invoices;

import com.invoices.model.Config;
import com.invoices.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
