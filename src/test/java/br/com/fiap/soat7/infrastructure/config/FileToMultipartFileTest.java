package br.com.fiap.soat7.infrastructure.config;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileToMultipartFileTest {

    /**
     * Test class for the {@link FileToMultipartFile} class, specifically testing
     * the {@link FileToMultipartFile#getName()} method.
     */

    @Test
    void shouldReturnCorrectFileName() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test-file", ".txt");
        tempFile.deleteOnExit();
        FileToMultipartFile fileToMultipartFile = new FileToMultipartFile(tempFile);

        // Act
        String fileName = fileToMultipartFile.getName();

        // Assert
        assertEquals(tempFile.getName(), fileName);
    }
}