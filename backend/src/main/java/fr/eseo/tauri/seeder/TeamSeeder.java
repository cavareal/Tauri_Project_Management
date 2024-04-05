package fr.eseo.tauri.seeder;

import net.datafaker.Faker;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamSeeder {

	private final TeamRepository teamRepository;

	@Autowired
	public TeamSeeder(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	public void seed(Faker faker) {
		for (int i = 0; i < 6; i++) {
			var team = new Team();
			team.name(faker.team().name());
			teamRepository.save(team);
		}
	}
}
