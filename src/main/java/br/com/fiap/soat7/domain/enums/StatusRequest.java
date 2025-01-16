package br.com.fiap.soat7.domain.enums;

import lombok.Getter;

@Getter
public enum StatusRequest {
	PROCESS_VIDEO_STATUS("video/process/status"),
	PROCESS_VIDEO_QUEUE("video/process/queue");

	private final String endPoint;
	StatusRequest(String endPoint) {
		this.endPoint = endPoint;
	}
}
