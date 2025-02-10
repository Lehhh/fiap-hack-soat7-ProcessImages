package br.com.fiap.soat7.infrastructure.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class DiskUtilsTest {

    private DiskUtils diskUtils;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        diskUtils = new DiskUtils();
        tempDir = Files.createTempDirectory("testDir");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testListFilesAsMultipartFile() throws IOException {
        // Criação de arquivos de teste
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.txt"));

        List<MultipartFile> multipartFiles = diskUtils.listFilesAsMultipartFile(tempDir);

        assertNotNull(multipartFiles);
        assertEquals(2, multipartFiles.size());
        assertTrue(multipartFiles.stream().anyMatch(file -> file.getOriginalFilename().equals("file1.txt")));
        assertTrue(multipartFiles.stream().anyMatch(file -> file.getOriginalFilename().equals("file2.txt")));
    }

    @Test
    void testCreateFolder() {
        String newFolderPath = tempDir.resolve("newFolder").toString();
        diskUtils.createFolder(newFolderPath);

        File newFolder = new File(newFolderPath);
        assertTrue(newFolder.exists() && newFolder.isDirectory());
    }

    @Test
    void testZipFolder() throws IOException {
        // Criação de arquivos de teste
        Files.writeString(tempDir.resolve("file1.txt"), "Conteudo do arquivo 1");
        Files.writeString(tempDir.resolve("file2.txt"), "Conteudo do arquivo 2");

        String zipFilePath = tempDir.resolve("test.zip").toString();
        diskUtils.zipFolder(tempDir.toString(), zipFilePath);

        // Verificação do conteúdo do ZIP
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            int fileCount = 0;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    fileCount++;
                }
            }
            assertEquals(3, fileCount);
        }
    }

    @Test
    void testZipFolder_WithNestedFolders_CreatesValidZip() throws IOException {
        // Arrange: Create nested structure
        Path nestedDir = Files.createDirectory(tempDir.resolve("nested"));
        Files.writeString(tempDir.resolve("file1.txt"), "Conteudo do arquivo 1");
        Files.writeString(nestedDir.resolve("file2.txt"), "Conteudo do arquivo 2");

        String zipFilePath = tempDir.resolve("nested.zip").toString();

        // Act
        diskUtils.zipFolder(tempDir.toString(), zipFilePath);

        // Assert
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            int fileCount = 0;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    fileCount++;
                }
            }
            assertEquals(3, fileCount, "ZIP should contain all files.");
        }
    }

    @Test
    void testZipFolder_ThrowsRuntimeException_OnIOException() {
        // Arrange
        String invalidSourceDir = "/invalid/source/dir";
        String zipFilePath = "/invalid/target.zip";

        // Act & Assert
        assertThrows(IOException.class, () -> diskUtils.zipFolder(invalidSourceDir, zipFilePath));
    }

    @Test
    void testListFilesAsMultipartFile_WithValidFiles_ReturnsMultipartFileList() throws IOException {
        // Arrange
        DiskUtils diskUtils = new DiskUtils();
        Path testFolderPath = Files.createTempDirectory("testFolder");
        File tempFile1 = Files.createTempFile(testFolderPath, "file1", ".txt").toFile();
        File tempFile2 = Files.createTempFile(testFolderPath, "file2", ".txt").toFile();

        // Act
        List<MultipartFile> result = diskUtils.listFilesAsMultipartFile(testFolderPath);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof MultipartFile);
        assertTrue(result.get(1) instanceof MultipartFile);

        // Cleanup
        tempFile1.delete();
        tempFile2.delete();
        Files.delete(testFolderPath);
    }

    @Test
    void testListFilesAsMultipartFile_WithEmptyDirectory_ReturnsEmptyList() throws IOException {
        // Arrange
        DiskUtils diskUtils = new DiskUtils();
        Path emptyFolderPath = Files.createTempDirectory("emptyFolder");

        // Act
        List<MultipartFile> result = diskUtils.listFilesAsMultipartFile(emptyFolderPath);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Cleanup
        Files.delete(emptyFolderPath);
    }

    @Test
    void testListFilesAsMultipartFile_WithIOException_ReturnsEmptyList() {
        // Arrange
        DiskUtils diskUtils = new DiskUtils(); // Class under test

        // Mock the static Files.list() method to throw an IOException
        Path invalidPath = mock(Path.class);
        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.list(invalidPath)).thenThrow(new IOException("Cannot access path"));

            // Act
            List<MultipartFile> result = diskUtils.listFilesAsMultipartFile(invalidPath);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
