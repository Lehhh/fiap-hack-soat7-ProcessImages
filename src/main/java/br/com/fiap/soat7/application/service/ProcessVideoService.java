package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.config.DiskUtils;
import br.com.fiap.soat7.infrastructure.config.VideoProcessProperties;
import br.com.fiap.soat7.infrastructure.config.VideoProcessing;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessVideoService {

	private final RedisService redisService;
	private final VideoProcessing videoProcessing;
	private final DiskUtils diskUtils;

	@Value("${br.com.fiap.soat7.upload.dir}")
	private String diskFolder;

	public void processQueue(){
		redisService.fetchQueueVideoProcess().forEach(v -> {
			String[] values = v.split(":");
			String userId = values[0];
			String videoId = values[1];
			String version = values[3];
			List<MultipartFile> multipartFiles = diskUtils.listFilesAsMultipartFile(Path.of(String.format(diskFolder, userId,videoId,version)));
			List<MultipartFile> multipartFilesVideo = multipartFiles.stream().filter(m -> !m.getOriginalFilename().contains("images") && !m.getOriginalFilename().startsWith(".")).toList();

			redisService.sendStatus(new InfoVideo(userId, videoId, version, Stage.PROCESS_VIDEO_IN_PROGRESS), StatusRequest.PROCESS_VIDEO_STATUS);
			try {
				diskUtils.createFolder(String.format(diskFolder, userId,videoId,version) + "images");
				videoProcessing.extractImageFromVideo(String.format(diskFolder, userId,videoId,version) + multipartFilesVideo.get(0).getOriginalFilename(), String.format(diskFolder, userId,videoId,version) + "images");
				diskUtils.zipFolder(String.format(diskFolder, userId,videoId,version) + "images", String.format(diskFolder, userId,videoId,version)  + "images.zip");
				redisService.sendStatus(new InfoVideo(userId, videoId, version, Stage.PROCESS_VIDEO_DONE), StatusRequest.PROCESS_VIDEO_STATUS);
			} catch (Exception e) {
				redisService.sendStatus(new InfoVideo(userId, videoId, version, Stage.PROCESS_VIDEO_ERROR), StatusRequest.PROCESS_VIDEO_STATUS);
				throw new RuntimeException(e);
			}
		});
	}

}
