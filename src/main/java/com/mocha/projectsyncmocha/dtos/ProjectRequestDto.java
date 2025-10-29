package com.capuccino.projectsynccapuccino.dtos;

public class ProjectRequestDto {
    private String name;
    private String description;
    private String status;
    private String responsible;

    public ProjectRequestDto() {
    }

    public ProjectRequestDto(String name, String description, String status, String responsible) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.responsible = responsible;
    }

    // Getters y setters
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

}
