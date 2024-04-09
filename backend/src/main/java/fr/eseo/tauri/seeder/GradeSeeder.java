package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Grade;
import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.*;
import fr.eseo.tauri.service.GradeService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeSeeder {

	private final GradeRepository gradeRepository;
	private final GradeTypeRepository gradeTypeRepository;
	private final StudentRepository studentRepository;
	private final GradeService gradeService;
	private final TeamRepository teamRepository;
	private final UserRepository userRepository;

	@Autowired
	public GradeSeeder(GradeRepository gradeRepository, GradeTypeRepository gradeTypeRepository, StudentRepository studentRepository, GradeService gradeService, TeamRepository teamRepository, UserRepository userRepository) {
		this.gradeRepository = gradeRepository;
		this.gradeTypeRepository = gradeTypeRepository;
		this.studentRepository = studentRepository;
		this.gradeService = gradeService;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

	public void seed(Faker faker) {
		var students = studentRepository.findAll();
		var gradeTypes = gradeTypeRepository.findAllImported();

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

		gradeService.updateImportedMean();
	}

	public void seedTeamGrades(Faker faker) {
		var gradeTypes = gradeTypeRepository.findAllNotImportedTeamGrades();
		var teams = teamRepository.findAll();
		var users = userRepository.findAll();
		for (var team : teams) {
			for (var gradeType : gradeTypes) {
				for (var user : users) {
					var grade = new Grade();
					grade.team(team);
					grade.gradeType(gradeType);
					grade.author(user);
					grade.value((float) faker.number().randomDouble(2, 0, 20));
					gradeRepository.save(grade);
				}
			}
		}
	}


}
