package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Student;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.enumeration.Gender;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.ProjectRepository;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.TeamRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class StudentSeeder {

	private final StudentRepository studentRepository;
	private final ProjectRepository projectRepository;

	private final TeamRepository teamRepository;

	@Autowired
	public StudentSeeder(StudentRepository studentRepository, ProjectRepository projectRepository, TeamRepository teamRepository) {
		this.studentRepository = studentRepository;
		this.projectRepository = projectRepository;
		this.teamRepository = teamRepository;
	}

	public void seed(Faker faker) {
		var project = projectRepository.findAll().get(0);
		List<Team> teams = teamRepository.findAll();
		Random random = new Random();

		for (Team team : teams) {
			team.project(project);
			teamRepository.save(team);
		}

		for (int i = 0; i < 60; i++) {
			var student = new Student();
			int teamId = random.nextInt(teams.size());

			student.name(faker.name().fullName());
			student.email(faker.internet().emailAddress());
			student.password(faker.internet().password());
			student.privateKey(faker.number().digits(20));
			student.gender(faker.number().numberBetween(0, 8) == 0 ? Gender.WOMAN : Gender.MAN);
			student.bachelor(faker.number().numberBetween(0, 8) == 0);
			student.teamRole("TEAM_MEMBER");
			student.team(teams.get(teamId));
			student.project(project);

			studentRepository.save(student);
		}
	}

}
