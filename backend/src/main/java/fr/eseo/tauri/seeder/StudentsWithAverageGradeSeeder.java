package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.repository.StudentRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class StudentsWithAverageGradeSeeder {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final GradeTypeRepository gradeTypeRepository;

    private final SecureRandom secureRandom = new SecureRandom();


    public StudentsWithAverageGradeSeeder(StudentRepository studentRepository, GradeRepository gradeRepository, GradeTypeRepository gradeTypeRepository) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.gradeTypeRepository = gradeTypeRepository;
    }

    public void seed(Faker faker) {

        int ratioWomen = 20; // in percent
        int ratioBachelor = 20; // in percent
        int nbStudents = 48;


        Gender[] genders = {Gender.MAN, Gender.WOMAN};

        // Create and save a gradeType
        var gradeType = new GradeType();
        gradeType.name("AVERAGE");
        gradeType.forGroup(false);
        gradeType.imported(true);
        this.gradeTypeRepository.save(gradeType);

        for (int i = 0; i < nbStudents; i++) {
            // Create and save a student
            int genderIndex = secureRandom.nextInt(100) < ratioWomen ? 1 : 0;
            var student = new Student();
            student.name(faker.name().fullName());
            student.email(faker.internet().emailAddress());
            student.password(faker.internet().password());
            student.privateKey(faker.number().digits(20));
            student.gender(genders[genderIndex]);
            student.bachelor(secureRandom.nextInt(100) < ratioBachelor);
            this.studentRepository.save(student);

            // Create and save a grade for the student
            var grade = new Grade();
            grade.student(student);
            grade.value((float) faker.number().randomDouble(2,0, 20));
            grade.comment(faker.lorem().sentence());
            grade.gradeType(gradeType);
            this.gradeRepository.save(grade);
        }
    }
}
