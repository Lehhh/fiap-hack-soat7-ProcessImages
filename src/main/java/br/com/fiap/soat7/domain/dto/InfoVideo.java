package br.com.fiap.soat7.domain.dto;

import br.com.fiap.soat7.domain.enums.Stage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InfoVideo {
	private Long videoId;
	private Long userId;
	private Long imageId =0L;
	private Integer version;
	private Stage stage;

	public InfoVideo(String videoId, String userId, String version, Stage stage) {
		this.videoId = Long.valueOf(videoId);
		this.userId = Long.valueOf(userId);
		this.version = Integer.valueOf(version);
		this.stage = stage;
	}
}
