package fr.eseo.tauri.controller;

import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fr.eseo.tauri.controller.StudentController;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentControllerTest {

    @Test
    public void testImportFiles() {

        StudentController student=new StudentController();

        Path path = Paths.get("C:\\Users\\pallu\\OneDrive\\Documents\\Workspace\\Ingenieur\\E4 n2\\ProjetGL\\nath\\example.csv");
        String name = "example.csv";
        String originalFileName = "example.csv";
        String contentType = MimeTypeUtils.ALL_VALUE;
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }

        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);

        File file2 = student.handleFileUpload(result);
        List<String> list = student.fileReader(file2);
    }
}