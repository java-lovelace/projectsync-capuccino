package com.capuccino.projectsynccapuccino.controllers;

import com.capuccino.projectsynccapuccino.dtos.ProjectDto;
import com.capuccino.projectsynccapuccino.dtos.ProjectRequestDto;
import com.capuccino.projectsynccapuccino.models.Project;
import com.capuccino.projectsynccapuccino.repositories.ProjectRepository;
import com.capuccino.projectsynccapuccino.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectRepository repo, ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProjectDto> getAll(){
        return service.getAll();
    }

    @GetMapping("{id}")
    public ProjectDto getProject(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    public ProjectDto save(@RequestBody ProjectRequestDto project){
        return service.create(project);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestDto projectDetails) {

            ProjectDto updated = service.updatePartial(id, projectDetails);
            return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
