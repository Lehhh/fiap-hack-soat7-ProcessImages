package br.com.fiap.soat7.application.service;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.config.VideoProcessProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class RedisServiceTest {

    private static final HttpEntity<?> EMPTY_HTTP_ENTITY = new HttpEntity<>(null);
    private static final ParameterizedTypeReference<List<String>> STRING_LIST_TYPE_REFERENCE = new ParameterizedTypeReference<>() {
    };

    @InjectMocks
    private RedisService redisService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private VideoProcessProperties props;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
        when(props.getRedisMidUrl()).thenReturn("http://mocked-url.com");

    }

    @Test
    void fetchQueueVideoProcess_ShouldReturnListOfVideos_WhenQueueIsNotEmpty() {
        // Arrange
        String mockedRedisUrl = "http://mocked-url.com";
        List<String> expectedVideos = List.of("video1", "video2");
        mockRestTemplate(mockedRedisUrl, expectedVideos);

        // Act
        List<String> actualVideos = redisService.fetchQueueVideoProcess();

        // Assert
        assertEquals(expectedVideos, actualVideos, "The videos should match the expected response.");
        verifyRestTemplateCalled();
    }

    @Test
    void fetchQueueVideoProcess_ShouldReturnEmptyList_WhenQueueIsEmpty() {
        // Arrange
        String mockRedisUrl = "http://mock-redis-url";
        List<String> mockedResponse = List.of();
        mockRestTemplate(mockRedisUrl, mockedResponse);

        // Act
        List<String> result = redisService.fetchQueueVideoProcess();

        // Assert
        assertEquals(mockedResponse, result);
        verifyRestTemplateCalled();
    }

    @Test
    void fetchQueueVideoProcess_ShouldThrowException_WhenRestTemplateThrowsException() {
        // Arrange
        String mockRedisUrl = "http://mock-redis-url";
        when(props.getRedisMidUrl()).thenReturn(mockRedisUrl);
        when(restTemplate.exchange(anyString(),
                eq(HttpMethod.GET),
                eq(EMPTY_HTTP_ENTITY),
                eq(STRING_LIST_TYPE_REFERENCE)))
                .thenThrow(new RuntimeException("Redis server error"));

        // Act & Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> redisService.fetchQueueVideoProcess());

        assertEquals("Redis server error", exception.getMessage());
        verifyRestTemplateCalled();
    }

    private void mockRestTemplate(String redisUrl, List<String> responseBody) {
        when(props.getRedisMidUrl()).thenReturn(redisUrl);
        when(restTemplate.exchange(any(String.class),
                eq(HttpMethod.GET),
                eq(EMPTY_HTTP_ENTITY),
                eq(STRING_LIST_TYPE_REFERENCE)))
                .thenReturn(ResponseEntity.ok(responseBody));

    }

    private void verifyRestTemplateCalled() {
        verify(restTemplate, times(1)).exchange(anyString(),
                eq(HttpMethod.GET),
                eq(EMPTY_HTTP_ENTITY),
                eq(STRING_LIST_TYPE_REFERENCE));
    }

    @Test
    void testSendStatus_ShouldPostStatusSuccessfully() {
        // Arrange
        InfoVideo infoVideo = new InfoVideo("1", "2", "1", null); // Dummy InfoVideo object
        StatusRequest statusRequest = StatusRequest.PROCESS_VIDEO_STATUS;
        String mockRedisUrl = "http://mock-redis-url";
        when(props.getRedisMidUrl()).thenReturn(mockRedisUrl);
        when(restTemplate.exchange(eq(mockRedisUrl + statusRequest.getEndPoint()),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)))
                .thenReturn(ResponseEntity.ok("Success"));

        // Act
        redisService.sendStatus(infoVideo, statusRequest);

        // Assert
        verify(restTemplate, times(1)).exchange(eq(mockRedisUrl + statusRequest.getEndPoint()),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class));
    }

    @Test
    void testSendStatus_ShouldThrowException_WhenRestTemplateThrowsException() {
        // Arrange
        InfoVideo infoVideo = new InfoVideo("1", "2", "1", null); // Dummy InfoVideo object
        StatusRequest statusRequest = StatusRequest.PROCESS_VIDEO_STATUS;
        String mockRedisUrl = "http://mock-redis-url";
        when(props.getRedisMidUrl()).thenReturn(mockRedisUrl);
        when(restTemplate.exchange(eq(mockRedisUrl + statusRequest.getEndPoint()),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)))
                .thenThrow(new RuntimeException("RestTemplate error"));

        // Act & Assert
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> redisService.sendStatus(infoVideo, statusRequest));

        assertEquals("400 RestTemplate error", exception.getMessage());
        verify(restTemplate, times(1)).exchange(eq(mockRedisUrl + statusRequest.getEndPoint()),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class));
    }
}