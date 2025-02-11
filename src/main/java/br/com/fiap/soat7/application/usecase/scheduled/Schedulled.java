package br.com.fiap.soat7.application.usecase.scheduled;

import br.com.fiap.soat7.application.service.ProcessVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Schedulled {
    private final ProcessVideoService processVideoService;

    @Scheduled
    public void executeProcessVideo() {
        processVideoService.processQueue();
    }

}
