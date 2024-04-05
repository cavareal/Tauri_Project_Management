package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSeeder {

	private final StudentRepository studentRepository;
	private final ProjectRepository projectRepository;

	@Autowired
	public StudentSeeder(StudentRepository studentRepository, ProjectRepository projectRepository) {
		this.studentRepository = studentRepository;
		this.projectRepository = projectRepository;
	}

	public void seed(Faker faker) {
		var project = projectRepository.findAll().get(0);

		for (int i = 0; i < 60; i++) {
			var student = new Student();

			student.name(faker.name().fullName());
			student.email(faker.internet().emailAddress());
			student.password(faker.internet().password());
			student.privateKey(faker.number().digits(20));
			student.gender(faker.number().numberBetween(0, 8) == 0 ? Gender.WOMAN : Gender.MAN);
			student.bachelor(faker.number().numberBetween(0, 8) == 0);
			student.teamRole("");
			student.team(null);
			student.project(project);

			studentRepository.save(student);
		}
	}

}
