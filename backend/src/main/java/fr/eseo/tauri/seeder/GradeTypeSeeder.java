package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeTypeSeeder {

	private static final String[] NAMES = {
			"PADL",
			"PDLO",
			"PWND",
			"IRS",
			"Stage S7",
			"S5",
			"S6"
	};

	private final GradeTypeRepository gradeTypeRepository;

	@Autowired
	public GradeTypeSeeder(GradeTypeRepository gradeTypeRepository) {
		this.gradeTypeRepository = gradeTypeRepository;
	}

	public void seed(Faker faker) {
		for (String name : NAMES) {
			var gradeType = new GradeType();
			gradeType.name(name);
			gradeType.factor((float) faker.number().numberBetween(1, 4));
			gradeType.forGroup(false);
			gradeType.imported(true);
			gradeTypeRepository.save(gradeType);
		}

		var importedMean = new GradeType();
		importedMean.name("mean");
		importedMean.factor(1f);
		importedMean.forGroup(false);
		importedMean.imported(true);
		gradeTypeRepository.save(importedMean);
	}

}
