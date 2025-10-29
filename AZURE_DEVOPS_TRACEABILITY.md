# Azure DevOps Traceability Template

## Epics
- EPIC-1: Core CRUD and persistence
  - Feature: REST API for projects
  - Feature: Database integration (MySQL) and ORM (JPA/Hibernate)
- EPIC-2: Frontend management
  - Feature: Bootstrap UI to create/read/update/delete projects
- EPIC-3: Testing and quality
  - Feature: Unit and integration tests
- EPIC-4: DevOps and traceability
  - Feature: Git Flow branches, PR, and Azure Boards work item linking

## Sample User Story (US-101)
**Title:** As a user I want to create a project so that it is tracked in the system.
**Acceptance criteria:**
- POST /api/projects returns 201 with created entity.
- Required fields validated.
- Project appears in the frontend list.

## How to link work items
- Create a Work Item in Azure Boards (e.g., US-101).
- When creating a branch, include the Work Item ID in the branch name: `feature/US-101-create-project`.
- Link commits and PRs to the work item using Azure DevOps UI or by including `AB#<workitem-id>` in commit messages.

## Workflow
New -> Active -> Resolved -> Closed
