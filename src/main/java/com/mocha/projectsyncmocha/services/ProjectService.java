package com.capuccino.projectsynccapuccino.services;

import com.capuccino.projectsynccapuccino.dtos.ProjectDto;
import com.capuccino.projectsynccapuccino.dtos.ProjectRequestDto;
import com.capuccino.projectsynccapuccino.models.Project;
import com.capuccino.projectsynccapuccino.repositories.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo){
        this.repo = repo;
    }

    private ProjectDto toDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getResponsible(),
                project.getCreated_at(),
                project.getUpdated_at()
        );
    }

    private Project fromDTO(ProjectRequestDto dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStatus(dto.getStatus());
        project.setResponsible(dto.getResponsible());
        return project;
    }

    public List<ProjectDto> getAll(){
        List<Project> projects = repo.findAll();
        List<ProjectDto> dtos = new ArrayList<>();
        for (Project project : projects) {
            dtos.add(toDto(project));
        }
        return dtos;
    }

    public ProjectDto getById(Long id){
         Project project = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
         return toDto(project);
    }

    public ProjectDto create(ProjectRequestDto dto){
        Project project = fromDTO(dto);
        Project saved = repo.save(project);
        return toDto(saved);
    }

    public ProjectDto updatePartial(Long id, ProjectRequestDto projectDetails){
        Project project = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        if (projectDetails.getName() != null) project.setName(projectDetails.getName());
        if (projectDetails.getDescription() != null) project.setDescription(projectDetails.getDescription());
        if (projectDetails.getStatus() != null) project.setStatus(projectDetails.getStatus());
        if (projectDetails.getResponsible() != null) project.setResponsible(projectDetails.getResponsible());
        Project updated = repo.save(project);
        return toDto(updated);
    }

    public void delete(Long id){
        if (!repo.existsById(id)) throw new EntityNotFoundException("Project not found");
        repo.deleteById(id);
    }
}
