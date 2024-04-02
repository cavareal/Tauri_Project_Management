package fr.eseo.tauri.service;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringBootTest
@Transactional
public class StudentServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Test
    public void testPopulateDatabaseFromCsv() {
        // Define a CSV file path
        String filePath = "C:\\Users\\coren\\Downloads\\Equipes1.csv";

        // Define the expected behavior of the mock objects
        Mockito.when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        // Call the method to test
        studentService.populateDatabaseFromCsv(filePath);

        // Verify that the methods of the mock objects were called
        Mockito.verify(userRepository, times(48)).save(any(User.class));
        Mockito.verify(studentRepository, times(48)).save(any(Student.class));
    }

//    public void createUser(User user){
//        System.out.println(userService.createUser(user) + "Corentin");
//    }
//    public static void main(String[] args) {
//        // Define a CSV file path
////        String filePath = "C:\\Users\\coren\\Downloads\\Equipes1.csv";
////
////        // Call the method to test
////        studentService.populateDatabaseFromCsv(filePath);
//        User user = new User();
//        user.email("test");
//        user.privateKey("test");
//        user.name("test");
//        user.password("test");
//        StudentServiceTest test = new StudentServiceTest();
//        test.createUser(user);
//    }
}