package com.capuccino.projectsynccapuccino.repositories;

import com.capuccino.projectsynccapuccino.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
}
