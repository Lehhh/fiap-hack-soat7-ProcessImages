package br.com.fiap.soat7.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileToMultipartFileTest {

    @Test
    void testTransferTo_withNonExistentDestination() throws IOException {
        // Arrange
        String content = "Content to transfer.";
        File sourceFile = File.createTempFile("sourceTransfer", ".txt");
        sourceFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }

        File destinationFile = new File(sourceFile.getParent(), "destination.txt");
        destinationFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(sourceFile);

        // Act
        multipartFile.transferTo(destinationFile);

        // Assert
        assertTrue(destinationFile.exists());
        assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), FileCopyUtils.copyToByteArray(destinationFile));
    }

    @Test
    void testTransferTo_withEmptySourceFile() throws IOException {
        // Arrange
        File sourceFile = File.createTempFile("emptySourceTransfer", ".txt");
        sourceFile.deleteOnExit();
        File destinationFile = new File(sourceFile.getParent(), "emptyDestination.txt");
        destinationFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(sourceFile);

        // Act
        multipartFile.transferTo(destinationFile);

        // Assert
        assertTrue(destinationFile.exists());
        assertEquals(0, destinationFile.length());
    }

//    @Test
//    void testTransferTo_withUnwritableDestination() {
//        // Arrange
//        File sourceFile = new File(System.getProperty("java.io.tmpdir"), "sourceUnwritable.txt");
//        File destinationFile = new File("/root/unwritable.txt");
//        sourceFile.deleteOnExit();
//
//        try {
//            if (sourceFile.createNewFile()) {
//                try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
//                    fos.write("Content for unwritable test.".getBytes(StandardCharsets.UTF_8));
//                }
//            }
//        } catch (IOException e) {
//            fail("Unable to create source file for unwritable test.");
//        }
//
//        FileToMultipartFile multipartFile = new FileToMultipartFile(sourceFile);
//
//        // Act & Assert
//        assertThrows(IOException.class, () -> multipartFile.transferTo(destinationFile));
//    }

    @Test
    void testGetBytes_withNonEmptyFile() throws IOException {
        // Arrange
        String content = "Test content for getBytes method.";
        File tempFile = File.createTempFile("testGetBytes", ".txt");
        tempFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }

        FileToMultipartFile multipartFile = new FileToMultipartFile(tempFile);

        // Act
        byte[] result = multipartFile.getBytes();

        // Assert
        assertNotNull(result);
        assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), result);
    }

    @Test
    void testGetBytes_withEmptyFile() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("testEmptyGetBytes", ".txt");
        tempFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(tempFile);

        // Act
        byte[] result = multipartFile.getBytes();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testGetBytes_withNonExistingFile() {
        // Arrange
        File nonExistentFile = new File("nonExistingFile.txt");
        FileToMultipartFile multipartFile = new FileToMultipartFile(nonExistentFile);

        // Act & Assert
        assertThrows(IOException.class, multipartFile::getBytes);
    }

    @Test
    void testGetBytes_withBinaryFile() throws IOException {
        // Arrange
        byte[] binaryContent = new byte[]{0x10, 0x20, 0x30, 0x40};
        File tempFile = File.createTempFile("testBinaryGetBytes", ".bin");
        tempFile.deleteOnExit();
        FileCopyUtils.copy(binaryContent, tempFile);

        FileToMultipartFile multipartFile = new FileToMultipartFile(tempFile);

        // Act
        byte[] result = multipartFile.getBytes();

        // Assert
        assertNotNull(result);
        assertArrayEquals(binaryContent, result);
    }

    @Test
    void testIsEmpty_withNonEmptyFile() throws IOException {
        // Arrange
        File nonEmptyFile = File.createTempFile("nonEmptyTest", ".txt");
        nonEmptyFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(nonEmptyFile)) {
            fos.write("Non-empty file content".getBytes(StandardCharsets.UTF_8));
        }

        FileToMultipartFile multipartFile = new FileToMultipartFile(nonEmptyFile);

        // Act
        boolean result = multipartFile.isEmpty();

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsEmpty_withEmptyFile() throws IOException {
        // Arrange
        File emptyFile = File.createTempFile("emptyTest", ".txt");
        emptyFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(emptyFile);

        // Act
        boolean result = multipartFile.isEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetSize_withNonEmptyFile() throws IOException {
        // Arrange
        String content = "Non-empty file content.";
        File nonEmptyFile = File.createTempFile("nonEmptyFile", ".txt");
        nonEmptyFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(nonEmptyFile)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }

        FileToMultipartFile multipartFile = new FileToMultipartFile(nonEmptyFile);

        // Act
        long size = multipartFile.getSize();

        // Assert
        assertEquals(content.length(), size);
    }

    @Test
    void testGetSize_withEmptyFile() throws IOException {
        // Arrange
        File emptyFile = File.createTempFile("emptyTestGetSize", ".txt");
        emptyFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(emptyFile);

        // Act
        long size = multipartFile.getSize();

        // Assert
        assertEquals(0, size);
    }

    @Test
    void testGetSize_withBinaryFile() throws IOException {
        // Arrange
        byte[] binaryContent = new byte[]{0x10, 0x20, 0x30, 0x40};
        File binaryFile = File.createTempFile("binaryFile", ".bin");
        binaryFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(binaryFile)) {
            fos.write(binaryContent);
        }

        FileToMultipartFile multipartFile = new FileToMultipartFile(binaryFile);

        // Act
        long size = multipartFile.getSize();

        // Assert
        assertEquals(binaryContent.length, size);
    }

    @Test
    void testGetSize_withNonExistentFile() {
        // Arrange
        File nonExistentFile = new File("nonExistentFileSizeTest.txt");
        FileToMultipartFile multipartFile = new FileToMultipartFile(nonExistentFile);

        // Act & Assert
        assertThrows(IOException.class, () -> multipartFile.getBytes());
    }

    @Test
    void testIsEmpty_withNonExistentFile() {
        // Arrange
        File nonExistentFile = new File("nonExistentTest.txt");
        FileToMultipartFile multipartFile = new FileToMultipartFile(nonExistentFile);

        // Act
        boolean result = multipartFile.isEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetName_withNonEmptyFile() throws IOException {
        // Arrange
        File nonEmptyFile = File.createTempFile("nonEmptyFile", ".txt");
        nonEmptyFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(nonEmptyFile)) {
            fos.write("Non-empty file content".getBytes(StandardCharsets.UTF_8));
        }

        FileToMultipartFile multipartFile = new FileToMultipartFile(nonEmptyFile);

        // Act
        String name = multipartFile.getName();

        // Assert
        assertTrue(name.contains("nonEmptyFile"));
    }

    @Test
    void testGetName_withEmptyFile() throws IOException {
        // Arrange
        File emptyFile = File.createTempFile("emptyFile", ".txt");
        emptyFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(emptyFile);

        // Act
        String name = multipartFile.getName();

        // Assert
        assertTrue(name.contains("emptyFile"));
    }

    @Test
    void testGetName_withNonExistentFile() {
        // Arrange
        File nonExistentFile = mock(File.class);
        when(nonExistentFile.getName()).thenReturn("mockedFile.txt");

        FileToMultipartFile multipartFile = new FileToMultipartFile(nonExistentFile);

        // Act
        String name = multipartFile.getName();

        // Assert
        assertEquals("mockedFile.txt", name);
    }

    @Test
    void testGetContentType_withGenericFile() throws IOException {
        // Arrange
        File genericFile = File.createTempFile("genericFile", ".dat");
        genericFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(genericFile);

        // Act
        String contentType = multipartFile.getContentType();

        // Assert
        assertEquals("application/octet-stream", contentType);
    }

    @Test
    void testGetContentType_withTextFile() throws IOException {
        // Arrange
        File textFile = File.createTempFile("textFile", ".txt");
        textFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write("Sample text content");
        }

        FileToMultipartFile multipartFile = new FileToMultipartFile(textFile);

        // Act
        String contentType = multipartFile.getContentType();

        // Assert
        assertEquals("application/octet-stream", contentType);
    }

    @Test
    void testGetContentType_withUnknownFileType() throws IOException {
        // Arrange
        File unknownFile = File.createTempFile("unknownFile", ".unknown");
        unknownFile.deleteOnExit();

        FileToMultipartFile multipartFile = new FileToMultipartFile(unknownFile);

        // Act
        String contentType = multipartFile.getContentType();

        // Assert
        assertEquals("application/octet-stream", contentType);
    }
}