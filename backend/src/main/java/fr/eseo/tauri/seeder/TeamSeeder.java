package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Student;
import net.datafaker.Faker;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamSeeder {

	private final TeamRepository teamRepository;
    private final StudentRepository studentRepository;

    public TeamSeeder(TeamRepository teamRepository, StudentRepository studentRepository) {
        this.teamRepository = teamRepository;
        this.studentRepository = studentRepository;
    }

    public void seed(Faker faker) {
        // Création des équipes
        for (int i = 0; i < 6; i++) {
            var team = new Team();
            team.name(faker.team().name());
            teamRepository.save(team);

//            // Ajout de 6 utilisateurs à chaque équipe
//            for (int j = 0; j < 6; j++) {
//                var student = new Student();
//                student.name(faker.name().fullName());
//                student.email(faker.internet().emailAddress());
//                student.password(faker.internet().password());
//                student.privateKey(faker.number().digits(20));
//                student.team(team);
//                studentRepository.save(student);
//            }
        }
    }
}
