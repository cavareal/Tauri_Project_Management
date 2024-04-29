package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.model.enumeration.ProjectPhase;
import fr.eseo.tauri.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectSeeder {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectSeeder(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void seed (){
        if (projectRepository.count() == 0) {
            // Add a default project if the table is empty
            projectRepository.save(new Project());
        }
    }
}
