package br.com.fiap.soat7.domain.enums;

import lombok.Getter;

@Getter
public enum Stage {
	PROCESS_VIDEO_QUEUE("process_video_queue"),
	PROCESS_VIDEO_IN_PROGRESS("process_video_in_progress"),
	PROCESS_VIDEO_DONE("process_video_done"),
	PROCESS_VIDEO_ERROR("process_video_error");

	private final String name;

	Stage(String name) {
		this.name = name;
	}
}
