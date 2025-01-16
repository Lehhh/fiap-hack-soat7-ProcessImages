package br.com.fiap.soat7.infrastructure.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Log4j2
public class DiskUtils {

	public List<MultipartFile> listFilesAsMultipartFile(Path folderPath) {
		List<MultipartFile> multipartFiles = new ArrayList<>();

		try {
			List<Path> files = Files.list(folderPath)
					.filter(Files::isRegularFile)
					.toList();

			// Convertendo cada arquivo para MultipartFile
			for (Path file : files) {
				FileSystemResource fileResource = new FileSystemResource(file.toFile());
				FileToMultipartFile multipartFile = new FileToMultipartFile(new File(String.valueOf(file)));
				multipartFiles.add(multipartFile);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return multipartFiles;
	}

	public String createFolder(String finalPath){
		File directory = new File(finalPath);
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				log.info("Diretório criado: " + directory.getAbsolutePath());
			} else {
				log.error("Falha ao criar o diretório: " + directory.getAbsolutePath());
				return TextReponse.UPLOAD_DISK_ERROR_FAIL_CREATE_DIRECTORY;
			}
		}
		return "Diretório criado: " + directory.getAbsolutePath();
	}

	public String zipFolder(String sourceDirPath, String zipFilePath) throws IOException {
		Path sourceDir = Paths.get(sourceDirPath);
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
			Files.walk(sourceDir)
					.filter(path -> !Files.isDirectory(path)) // Apenas arquivos, não diretórios
					.forEach(path -> {
						String zipEntryName = sourceDir.relativize(path).toString(); // Nome relativo no ZIP
						try {
							zipOutputStream.putNextEntry(new ZipEntry(zipEntryName));
							Files.copy(path, zipOutputStream);
							zipOutputStream.closeEntry();
						} catch (IOException e) {
							throw new RuntimeException("Erro ao criar o arquivo ZIP", e);
						}
					});
		}
		return zipFilePath;
	}
}
