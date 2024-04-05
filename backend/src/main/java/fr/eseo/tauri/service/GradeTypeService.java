package fr.eseo.tauri.service;

import fr.eseo.tauri.model.GradeType;
import fr.eseo.tauri.repository.GradeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeTypeService {

	private final GradeService gradeService;
	private final GradeTypeRepository gradeTypeRepository;

	@Autowired
	public GradeTypeService(GradeTypeRepository gradeTypeRepository, GradeService gradeService) {
		this.gradeTypeRepository = gradeTypeRepository;
		this.gradeService = gradeService;
	}

	public GradeType updateFactor(int id, float factor) {
		var gradeType = gradeTypeRepository.findById(id).orElse(null);
		if (gradeType == null) return null;

		gradeType.factor(factor);
		gradeTypeRepository.save(gradeType);

		gradeService.updateImportedMean();

		return gradeType;
	}

}
