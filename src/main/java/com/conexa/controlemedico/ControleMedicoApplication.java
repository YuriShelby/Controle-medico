package com.conexa.controlemedico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ControleMedicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleMedicoApplication.class, args);
	}

}
