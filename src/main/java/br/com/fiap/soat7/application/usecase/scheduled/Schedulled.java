package br.com.fiap.soat7.application.usecase.scheduled;

import br.com.fiap.soat7.application.service.ProcessVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class Schedulled {
    private final ProcessVideoService processVideoService;

    @Scheduled(fixedDelay = 3000)
    public void executeProcessVideo() {
        log.info("Iniciando processamento de video...");
        processVideoService.processQueue();
    }

}
