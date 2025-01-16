package br.com.fiap.soat7.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("br.com.fiap.soat7")
@Getter
@Setter
public class VideoProcessProperties {
	private String redisMidUrl;
}
