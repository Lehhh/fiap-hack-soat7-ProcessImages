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
}
