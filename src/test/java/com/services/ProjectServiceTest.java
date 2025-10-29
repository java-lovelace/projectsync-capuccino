package com.services;

import com.capuccino.projectsynccapuccino.dtos.ProjectDto;
import com.capuccino.projectsynccapuccino.dtos.ProjectRequestDto;
import com.capuccino.projectsynccapuccino.services.ProjectService;
import com.capuccino.projectsynccapuccino.models.Project;
import com.capuccino.projectsynccapuccino.repositories.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for ProjectService.
 * This test uses Mockito to mock the ProjectRepository
 * so we can test the business logic without touching the database.
 */
class ProjectServiceTest {

    private ProjectRepository repo; // Mocked repository
    private ProjectService service; // Real service instance (with mock injected)

    /**
     * Runs before each test method.
     * Creates a new mock repository and injects it into the service.
     */
    @BeforeEach
    void setUp() {
        repo = mock(ProjectRepository.class); // Create mock repository
        service = new ProjectService(repo);   // Inject mock into the service
    }

    /**
     * Test that getAll() returns all projects correctly.
     */
    @Test
    void testGetAll() {
        // Mock two projects as if they exist in the DB
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("P1");
        p1.setDescription("Desc1");
        p1.setStatus("Active");
        p1.setResponsible("John");

        Project p2 = new Project();
        p2.setId(2L);
        p2.setName("P2");
        p2.setDescription("Desc2");
        p2.setStatus("Inactive");
        p2.setResponsible("Jane");

        // When repo.findAll() is called, return this fake list
        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Execute the service method
        List<ProjectDto> result = service.getAll();

        // Verify the list has 2 elements and first name is correct
        assertEquals(2, result.size());
        assertEquals("P1", result.get(0).getName());

        // Verify that repo.findAll() was called exactly once
        verify(repo, times(1)).findAll();
    }

    /**
     * Test that getById() returns a project when the ID exists.
     */
    @Test
    void testGetByIdFound() {
        // Mock a project returned by repo
        Project project = new Project();
        project.setId(1L);
        project.setName("Test");
        project.setDescription("Desc");
        project.setStatus("Active");
        project.setResponsible("John");

        when(repo.findById(1L)).thenReturn(Optional.of(project));

        // Call service method
        ProjectDto result = service.getById(1L);

        // Assert the result is not null and matches the expected name
        assertNotNull(result);
        assertEquals("Test", result.getName());

        // Verify repo was queried with correct ID
        verify(repo).findById(1L);
    }

    /**
     * Test that getById() throws an exception if the project does not exist.
     */
    @Test
    void testGetByIdNotFound() {
        // Simulate that repo.findById() returns empty
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Expect EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> service.getById(99L));
    }

    /**
     * Test that create() correctly saves and returns a new project.
     */
    @Test
    void testCreateProject() {
        // Mock incoming request DTO
        ProjectRequestDto dto = new ProjectRequestDto("New", "Desc", "Active", "Sam");

        // Simulate what repo.save() would return
        Project saved = new Project();
        saved.setId(1L);
        saved.setName("New");
        saved.setDescription("Desc");
        saved.setStatus("Active");
        saved.setResponsible("Sam");


        // When repo.save() is called, return the saved project
        when(repo.save(any(Project.class))).thenReturn(saved);

        // Execute service method
        ProjectDto result = service.create(dto);

        // Verify returned values
        assertEquals("New", result.getName());
        assertEquals("Active", result.getStatus());

        // Verify repo.save() was called
        verify(repo).save(any(Project.class));
    }

    /**
     * Test that updatePartial() modifies only provided fields.
     */
    @Test
    void testUpdatePartial() {
        // Existing project in repo
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old");
        existing.setDescription("Desc");
        existing.setStatus("Active");
        existing.setResponsible("Sam");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        // Mock save() to return whatever project it receives
        when(repo.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        // Create DTO with only one field updated
        ProjectRequestDto update = new ProjectRequestDto("Updated", null, null, null);

        // Call service method
        ProjectDto result = service.updatePartial(1L, update);

        // Verify only the name changed
        assertEquals("Updated", result.getName());

        // Verify repo.save() was called with the updated entity
        verify(repo).save(existing);
    }

    /**
     * Test that delete() removes a project if it exists.
     */
    @Test
    void testDeleteExisting() {
        // Simulate that project exists in DB
        when(repo.existsById(1L)).thenReturn(true);

        // Call service method
        service.delete(1L);

        // Verify that repo.deleteById() was called
        verify(repo).deleteById(1L);
    }

    /**
     * Test that delete() throws exception if project does not exist.
     */
    @Test
    void testDeleteNotFound() {
        // Simulate that project does NOT exist
        when(repo.existsById(99L)).thenReturn(false);

        // Expect EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> service.delete(99L));
    }
}

