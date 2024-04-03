package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Student;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import fr.eseo.tauri.repository.StudentRepository;

@Service
public class StudentSeeder {

    private final StudentRepository studentRepository;

    public StudentSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void seed(Faker faker) {
        for (int i = 0; i < 20; i++) {
            var student = new Student();
            student.name(faker.name().fullName());
            student.email(faker.internet().emailAddress());
            student.password(faker.internet().password());
            student.privateKey(faker.number().digits(20));
            studentRepository.save(student);
        }
    }
}
