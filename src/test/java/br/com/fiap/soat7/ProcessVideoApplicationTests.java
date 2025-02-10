package br.com.fiap.soat7;

import br.com.fiap.soat7.application.service.ProcessVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class ProcessVideoApplicationTest {

	@MockBean
	private ProcessVideoService processVideoService;

	@SpyBean
	private ProcessVideoApplication processVideoApplication;

	@Test
	void testRun_shouldCallProcessQueue() throws Exception {
		// Act
		processVideoApplication.run();

		// Assert
		verify(processVideoService, atLeastOnce()).processQueue();
	}
}
