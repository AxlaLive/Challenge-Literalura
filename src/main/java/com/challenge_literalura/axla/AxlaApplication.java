package com.challenge_literalura.axla;

import com.challenge_literalura.axla.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AxlaApplication implements CommandLineRunner {
	@Autowired
	private Principal principal;

	public static void main(String[] args) {

		SpringApplication.run(AxlaApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		principal.menu();
	}
}
