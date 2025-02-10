package br.com.fiap.soat7.application.service;


import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.config.DiskUtils;
import br.com.fiap.soat7.infrastructure.config.VideoProcessing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProcessVideoServiceTest {

    @InjectMocks
    private ProcessVideoService processVideoService;

    @Mock
    private RedisService redisService;

    @Mock
    private VideoProcessing videoProcessing;

    @Mock
    private DiskUtils diskUtils;

    @Value("${br.com.fiap.soat7.upload.dir}")
    private String diskFolder;

    public ProcessVideoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUp() {
        diskFolder = "src/test/resources/upload/";
        ReflectionTestUtils.setField(processVideoService, "diskFolder", diskFolder);
    }

    @Test
    void shouldProcessVideosSuccessfully() throws Exception {
        // Extract constants for readability
        final String USER_ID = "123";
        final String VIDEO_ID = "456";
        final String VERSION = "789";
        final String VIDEO_QUEUE_ENTRY = USER_ID + ":" + VIDEO_ID + "::" + VERSION;

        // Mock dependencies
        MultipartFile videoFileMock = mock(MultipartFile.class);
        when(videoFileMock.getOriginalFilename()).thenReturn("video.mp4");
        when(redisService.fetchQueueVideoProcess()).thenReturn(List.of(VIDEO_QUEUE_ENTRY));
        when(diskUtils.listFilesAsMultipartFile(Path.of(String.format(diskFolder, USER_ID, VIDEO_ID, VERSION))))
                .thenReturn(List.of(videoFileMock, videoFileMock));

        // Stubbing void methods
        doNothing().when(redisService).sendStatus(any(InfoVideo.class), eq(StatusRequest.PROCESS_VIDEO_STATUS));
        doNothing().when(diskUtils).createFolder(anyString());
        doNothing().when(diskUtils).zipFolder(anyString(), anyString());
        doNothing().when(videoProcessing).extractImageFromVideo(anyString(), anyString());

        // Run the method under test
        processVideoService.processQueue();

        // Assertions and verifications using extracted methods
        verify(redisService).fetchQueueVideoProcess();
        verifySendStatus(USER_ID, VIDEO_ID, VERSION, Stage.PROCESS_VIDEO_IN_PROGRESS);
        verify(videoProcessing, times(1)).extractImageFromVideo(anyString(), anyString());
        verify(diskUtils, times(1)).zipFolder(anyString(), anyString());
        verifySendStatus(USER_ID, VIDEO_ID, VERSION, Stage.PROCESS_VIDEO_DONE);
    }

    // Helper method to reduce duplication in verifying sendStatus calls
    private void verifySendStatus(String userId, String videoId, String version, Stage stage) {
        verify(redisService, times(2)).sendStatus(
                any(),
                eq(StatusRequest.PROCESS_VIDEO_STATUS)
        );
    }


    @Test
    void shouldHandleExceptionAndSendErrorStatus() throws Exception {
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String videoQueueEntry = userId + ":" + videoId + "::" + version;

        MultipartFile videoFile = mock(MultipartFile.class);
        when(videoFile.getOriginalFilename()).thenReturn("video.mp4");

        when(redisService.fetchQueueVideoProcess()).thenReturn(List.of(videoQueueEntry));
        when(diskUtils.listFilesAsMultipartFile(Path.of(String.format(diskFolder, userId, videoId, version))))
                .thenReturn(List.of(videoFile));

        doNothing().when(redisService).sendStatus(any(InfoVideo.class), eq(StatusRequest.PROCESS_VIDEO_STATUS));
        doThrow(RuntimeException.class).when(videoProcessing).extractImageFromVideo(anyString(), anyString());

        assertThrows(RuntimeException.class, () -> processVideoService.processQueue());

        verify(redisService, times(1)).fetchQueueVideoProcess();
        verify(redisService, times(2)).sendStatus(
                any(InfoVideo.class),
                any()
        );
    }

    @Test
    void shouldNotProcessWhenQueueIsEmpty() {
        when(redisService.fetchQueueVideoProcess()).thenReturn(List.of());

        processVideoService.processQueue();

        verify(redisService, times(1)).fetchQueueVideoProcess();
        verifyNoMoreInteractions(redisService);
        verifyNoInteractions(videoProcessing);
        verifyNoInteractions(diskUtils);
    }

}
