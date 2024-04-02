package fr.eseo.tauri.service;

import fr.eseo.tauri.repository.StudentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class StudentServiceTest {
//
//    @Mock
//    private StudentRepository studentRepository;
//
//    @InjectMocks
//    private StudentService studentService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("extractNamesGenderAndBachelor with valid data returns correct lists")
//    void extractNamesGenderAndBachelor_withValidData_returnsCorrectLists() {
//        String filePath = "valid.csv";
//        List<List<String>> result = StudentService.extractNamesGenderAndBachelor(filePath);
//
//        assertEquals(3, result.size());
//        assertEquals("John Doe", result.get(0).get(0));
//        assertEquals("M", result.get(1).get(0));
//        assertEquals("Yes", result.get(2).get(0));
//    }
//
//    @Test
//    @DisplayName("extractNamesGenderAndBachelor with invalid file path throws IllegalArgumentException")
//    void extractNamesGenderAndBachelor_withInvalidFilePath_throwsIllegalArgumentException() {
//        String filePath = "";
//        assertThrows(IllegalArgumentException.class, () -> StudentService.extractNamesGenderAndBachelor(filePath));
//    }
//
//    @Test
//    @DisplayName("extractNamesGenderAndBachelor with non-existent file throws IOException")
//    void extractNamesGenderAndBachelor_withNonExistentFile_throwsIOException() {
//        String filePath = "non_existent.csv";
//        assertThrows(IOException.class, () -> StudentService.extractNamesGenderAndBachelor(filePath));
//    }
}