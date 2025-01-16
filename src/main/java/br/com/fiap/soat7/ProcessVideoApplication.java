package br.com.fiap.soat7;

import br.com.fiap.soat7.application.service.ProcessVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ProcessVideoApplication implements CommandLineRunner {

	private final ProcessVideoService processVideoService;

	public static void main(String[] args) {
		SpringApplication.run(ProcessVideoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		processVideoService.processQueue();
//		String videoPath = "/Users/lst/Documents/Hackaton-fiap-2025/volume-compartilhado/1/1/2/Marvel_DOTNET_CSHARP.mp4";
//		String imagePath = "/Users/lst/Documents/Hackaton-fiap-2025/volume-compartilhado/1/1/2/images";
//		String imageZipPath = "/Users/lst/Documents/Hackaton-fiap-2025/volume-compartilhado/1/1/2/images.zip";
//
//		diskUtils.createFolder(imagePath);
//		videoProcessing.extractImageFromVideo(videoPath, imagePath);
//		diskUtils.zipFolder(imagePath, imageZipPath);
	}
}
