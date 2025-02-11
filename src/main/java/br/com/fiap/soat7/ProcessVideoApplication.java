package br.com.fiap.soat7;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ProcessVideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessVideoApplication.class, args);
	}

}
