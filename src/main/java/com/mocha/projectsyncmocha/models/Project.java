package com.capuccino.projectsynccapuccino.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name="projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String status;
    private String responsible;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = "NEW";
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
}
