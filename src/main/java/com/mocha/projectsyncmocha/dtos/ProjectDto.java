package com.capuccino.projectsynccapuccino.dtos;

import java.time.LocalDateTime;

public class ProjectDto {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String responsible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectDto() {
    }

    public ProjectDto(Long id, String name, String description, String status,
                      String responsible, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.responsible = responsible;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponsible() {
        return responsible;
    }
    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


