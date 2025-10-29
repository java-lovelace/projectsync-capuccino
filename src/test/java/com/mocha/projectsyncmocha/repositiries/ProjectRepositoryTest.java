package com.capuccino.projectsynccapuccino.repositiries;

import com.capuccino.projectsynccapuccino.models.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.capuccino.projectsynccapuccino.repositories.ProjectRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") // asegura que use tu config de test (H2, create-drop, etc.)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void save_shouldPersist_andSetDefaultsAndTimestamps() {
        // Dado: un project sin status (debe quedar "NEW" por @PrePersist)
        Project p = new Project();
        p.setName("Capuccino Backend");
        p.setDescription("Core services");

        // Cuando: guardamos
        Project saved = projectRepository.save(p);

        // Entonces: se asigna ID, status por defecto y timestamps
        assertNotNull(saved.getId());
        assertEquals("NEW", saved.getStatus(), "status debe inicializarse en NEW por @PrePersist");
        assertNotNull(saved.getCreated_at(), "created_at debe setearse en @PrePersist");
        assertNotNull(saved.getUpdated_at(), "updated_at debe setearse en @PrePersist");
    }

    @Test
    void findById_shouldReturnSavedEntity() {
        Project p = new Project();
        p.setName("Find Me");
        p.setDescription("Testing findById");
        Project saved = projectRepository.save(p);

        Optional<Project> found = projectRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Find Me", found.get().getName());
    }

    @Test
    void findAll_shouldReturnInsertedItems() {
        projectRepository.save(build("Frontend UI", "NEW", "UI module", "Alice"));
        projectRepository.save(build("DB Setup", "COMPLETED", "Init schema", "Bob"));

        List<Project> all = projectRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void update_shouldTouchUpdatedAt_viaPreUpdate() throws InterruptedException {
        Project p = projectRepository.save(build("To Update", "NEW", "v1", "Charles"));
        LocalDateTime firstUpdated = p.getUpdated_at();

        Thread.sleep(5); // solo para ver diferencia temporal
        p.setDescription("v2");
        projectRepository.save(p);

        // 🔑 Fuerza sincronización y vuelve a cargar
        projectRepository.flush();
        Project reloaded = projectRepository.findById(p.getId()).orElseThrow();

        assertNotNull(reloaded.getUpdated_at());
        assertTrue(reloaded.getUpdated_at().isAfter(firstUpdated),
                "updated_at debe actualizarse en @PreUpdate");
    }


    @Test
    void deleteById_shouldRemoveEntity() {
        Project p = projectRepository.save(build("Temp", "NEW", "to delete", "Alice"));
        Long id = p.getId();

        projectRepository.deleteById(id);

        assertFalse(projectRepository.findById(id).isPresent());
    }

    @Test
    void canPersistStatusExplicitly_withoutBeingOverwritten() {
        // Dado: seteamos status explícitamente antes de persistir
        Project p = new Project();
        p.setName("Explicit Status");
        p.setStatus("COMPLETED"); // @PrePersist NO debe sobrescribirlo
        Project saved = projectRepository.save(p);

        assertEquals("COMPLETED", saved.getStatus(),
                "Si status no es null, @PrePersist no debe cambiarlo");
    }

    private Project build(String name, String status, String description, String responsible) {
        Project p = new Project();
        p.setName(name);
        p.setStatus(status);           // si es null, @PrePersist lo pone en "NEW"
        p.setDescription(description);
        p.setResponsible(responsible); // en tu entidad es String (no relación)
        return p;
    }
}
