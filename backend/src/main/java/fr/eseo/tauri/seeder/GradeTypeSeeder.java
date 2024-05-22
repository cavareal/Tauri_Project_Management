package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.model.enumeration.GradeTypeName;
import fr.eseo.tauri.repository.GradeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeTypeSeeder {

	private final GradeTypeRepository gradeTypeRepository;

	GradeTypeName[] gradeTypeNames = GradeTypeName.values();

	public void seed() {
		for(GradeTypeName gradeTypeName : gradeTypeNames) {

			if(gradeTypeName != GradeTypeName.AVERAGE) {

				var gradeType = new GradeType();

				gradeType.name(gradeTypeName.displayName());
				gradeType.factor(1.f);
				gradeType.forGroup(true);
				gradeType.imported(false);

				if (gradeTypeName == GradeTypeName.INDIVIDUAL_PERFORMANCE) gradeType.forGroup(false);

				gradeTypeRepository.save(gradeType);
			}
		}
	}

}
