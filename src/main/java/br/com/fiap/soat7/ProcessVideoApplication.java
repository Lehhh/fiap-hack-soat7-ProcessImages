package br.com.fiap.soat7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProcessVideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessVideoApplication.class, args);
	}

}
