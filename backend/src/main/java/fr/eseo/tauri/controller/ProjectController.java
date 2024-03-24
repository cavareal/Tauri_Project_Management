package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Project;
import fr.eseo.tauri.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping("/add")
    public Project addProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Project updateProject(@PathVariable Integer id, @RequestBody Project projectDetails) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            project.nbTeams(projectDetails.nbTeams());
            project.ratioGendre(projectDetails.ratioGendre());
            project.nbSprint(projectDetails.nbSprint());
            project.phase(projectDetails.phase());
            return projectRepository.save(project);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProject(@PathVariable Integer id) {
        projectRepository.deleteById(id);
        return "Project deleted";
    }
}
