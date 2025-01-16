package br.com.fiap.soat7.infrastructure.config;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class VideoProcessing {

	public String extractImageFromVideo(String videoFilePath, String imageFilePath) throws Exception {
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFilePath);
		grabber.start();

		int frameRate = (int) grabber.getFrameRate(); // taxa de quadros por segundo do vídeo
		int frameCount = grabber.getLengthInFrames(); // total de quadros no vídeo
		int frameNumber = 0;

		// Calcular o intervalo em quadros para 1 segundo
		int intervalInFrames = frameRate;

		// Usando OpenCVFrameConverter para converter os frames para Mat
		OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

		while (frameNumber < frameCount) {
			// Avança o grabber para o quadro correspondente
			grabber.setFrameNumber(frameNumber);
			Frame frame = grabber.grabImage();
			if (frame == null) break; // Fim do vídeo

			// Converte o Frame para Mat
			Mat mat = converter.convert(frame);

			// Salva o quadro como uma imagem no diretório de saída
			String imagePath = imageFilePath + File.separator + "frame_" + frameNumber + ".png";
			opencv_imgcodecs.imwrite(imagePath, mat);

			System.out.println("Frame capturado e salvo em: " + imagePath);

			// Avança o número do quadro para o próximo intervalo de 1 segundo
			frameNumber += intervalInFrames;
		}

		grabber.stop();
		return imageFilePath;
	}
}
