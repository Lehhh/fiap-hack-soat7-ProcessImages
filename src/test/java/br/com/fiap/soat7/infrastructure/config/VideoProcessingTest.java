package br.com.fiap.soat7.infrastructure.config;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoProcessingTest {

    @Mock
    private FFmpegFrameGrabber grabber;

    @Mock
    private OpenCVFrameConverter.ToMat converter;

    @Mock
    private Frame frame;

    @Mock
    private Mat mat;

    @InjectMocks
    private VideoProcessing videoProcessing;

    @Test
    void testExtractImageFromVideo() throws Exception {
        String videoFilePath = "src/test/resources/test-video.mp4";
        String imageFilePath = "src/test/resources/output";

        // Criar diretório de saída se não existir
        Files.createDirectories(Paths.get(imageFilePath));

        // Iniciar o método
        videoProcessing.extractImageFromVideo(videoFilePath, imageFilePath);

        // Verificar se a imagem foi salva
        File outputDir = new File(imageFilePath);
        File[] files = outputDir.listFiles((dir, name) -> name.endsWith(".png"));
        assertNotNull(files);
        assertTrue(files.length > 0, "Nenhuma imagem foi gerada.");

        // Limpar os arquivos gerados após o teste
        for (File file : files) {
            file.delete();
        }
    }
}
