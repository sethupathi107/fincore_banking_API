package com.myself.FinCoreBankingApiApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication

@OpenAPIDefinition(
	info = @Info(
		title="FinCore Banking API",
		description="A Spring Boot–based backend system that enables secure account management and fund transfers with transaction handling, email notifications, and RESTful API design.",
		version="v1.0",
		contact=@Contact(
			name="Sethupathi",
			email="sethupathiofficial107@gmail.com",
			url="https://github.com/sethupathi107/fincore_banking_API"			
		),
		license = @License(
			name="General License",
			url="https://github.com/sethupathi107/fincore_banking_API"
		)
	),
	externalDocs=@ExternalDocumentation(
		description = "A Spring Boot–based backend system that enables secure account management and fund transfers with transaction handling, email notifications, and RESTful API design.",
		url="https://github.com/sethupathi107/fincore_banking_API"
	)
)
public class FinCoreBankingApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinCoreBankingApiApplication.class, args);
	}
}
