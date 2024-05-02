package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeTypeSeeder {

	private static final String[] NAMES_TEAM_GRADE = {
			"Performance Globale",
			"Support Matériel",
			"Contenu de la présentation",
			"Solution technique",
			"Gestion de projet",
			"Conformité au sprint"
	};

	private final GradeTypeRepository gradeTypeRepository;

	public void seed() {
		for (String name : NAMES_TEAM_GRADE) {
			var gradeType = new GradeType();

			gradeType.name(name);
			gradeType.factor(1.f);
			gradeType.forGroup(true);
			gradeType.imported(false);

			gradeTypeRepository.save(gradeType);
		}
	}

}
