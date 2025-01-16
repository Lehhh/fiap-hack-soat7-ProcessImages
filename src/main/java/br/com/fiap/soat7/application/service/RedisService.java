package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.config.VideoProcessProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisService {

	private final RestTemplate restTemplate;
	private final VideoProcessProperties props;


	public Boolean sendStatus(InfoVideo infoVideo, StatusRequest statusRequest){
		try{
			ResponseEntity<String> exchange = restTemplate.exchange(props.getRedisMidUrl() + statusRequest.getEndPoint(),
					HttpMethod.POST,
					new HttpEntity<>(infoVideo),
					String.class);
			log.info(exchange.getBody());
			return true;
		}
		catch (Exception e){
			log.error(e.getMessage());
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public List<String> fetchQueueVideoProcess(){
		ResponseEntity<List<String>> exchange = restTemplate.exchange(props.getRedisMidUrl() + StatusRequest.PROCESS_VIDEO_QUEUE.getEndPoint(),
				HttpMethod.GET,
				new HttpEntity<>(null),
				new ParameterizedTypeReference<List<String>>() {}
		);
		return exchange.getBody();
	}
}
