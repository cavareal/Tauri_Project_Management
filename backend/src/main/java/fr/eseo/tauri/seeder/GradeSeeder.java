package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeRepository;
import fr.eseo.tauri.repository.GradeTypeRepository;
import fr.eseo.tauri.repository.StudentRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeSeeder {

	private final GradeRepository gradeRepository;
	private final GradeTypeRepository gradeTypeRepository;
	private final StudentRepository studentRepository;

	@Autowired
	public GradeSeeder(GradeRepository gradeRepository, GradeTypeRepository gradeTypeRepository, StudentRepository studentRepository) {
		this.gradeRepository = gradeRepository;
		this.gradeTypeRepository = gradeTypeRepository;
		this.studentRepository = studentRepository;
	}

	public void seed(Faker faker) {
		var students = studentRepository.findAll();
		var gradeTypes = gradeTypeRepository.findAll();

		for (var student : students) {
			for (var gradeType : gradeTypes) {
				if (Boolean.TRUE.equals(student.bachelor()) && !gradeType.name().equals("mean")) continue;

				var grade = new Grade();
				grade.student(student);
				grade.gradeType(gradeType);
				grade.value((float) faker.number().randomDouble(2,0, 20));
				gradeRepository.save(grade);
			}
		}
	}

}
