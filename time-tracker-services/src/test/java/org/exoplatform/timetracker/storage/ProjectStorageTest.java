package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.entity.ProjectEntity;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

public class ProjectStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateProject() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    assertNotNull(project);
    Project newProject = createProject(project);
    assertNotNull(newProject);
    // Throw
    assertThrows(IllegalArgumentException.class, () -> projectStorage.createProject(null));
  }

  @Test
  public void testUpdateProject() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    Project newProject = createProject(project);
    newProject.setDisplayLabel("testUpdate5");
    Project projectNotFound = projectStorage.getProjects().get(0);
    projectNotFound.setId(5l);

    Project newProjectUpdated = projectStorage.updateProject(newProject);

    // Throw
    assertNotNull(newProjectUpdated);
    assertThrows(IllegalArgumentException.class, () -> projectStorage.updateProject(null));
    assertThrows(EntityNotFoundException.class, () -> projectStorage.updateProject(projectNotFound));
  }

  @Test
  public void testDeleteProject() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    Project newProject = createProject(project);
    Long projectId = newProject.getId();
    Long projectIdNull = 0l;
    Long projectIdNotFound = 5l;
    cleanupClients.remove(clientNew);
    cleanupProjects.remove(newProject);
    projectStorage.deleteProject(projectId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> projectStorage.deleteProject(projectIdNull));
    assertThrows(EntityNotFoundException.class, () -> projectStorage.deleteProject(projectIdNotFound));

  }

  @Test
  public void testGetProjectById() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    Project projectNew = createProject(project);
    Long projectId = projectNew.getId();
    Long projectIdNull = 0l;

    Project newProject = projectStorage.getProjectById(projectId);

    // Throw
    assertNotNull(newProject);
    assertThrows(IllegalArgumentException.class, () -> projectStorage.getProjectById(projectIdNull));

  }

  @Test
  public void testGetProjects() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    createProject(project);
    List<Project> projects = projectStorage.getProjects();
    assertNotNull(projects);
    assertTrue(!projects.isEmpty());

  }

  @Test
  public void testCountProjects() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    createProject(project);
    Long countProject = projectStorage.countProjects();
    assertNotNull(countProject);
  }

  @Test
  public void toDTO() {
    ProjectEntity projectEntityNull = null;
    Project projectDTO = projectStorage.toDTO(projectEntityNull);
    assertNull(projectDTO);
  }

  @Test
  public void toEntity() {
    Project projectNull = null;
    ProjectEntity projectEntity = projectStorage.toEntity(projectNull);
    assertNull(projectEntity);
  }

  private Project createProject(Project project) throws Exception {
    List<Project> projects = projectStorage.getProjects();
    assertNotNull(projects);
    Project storedProject = projectStorage.createProject(project);
    assertNotNull(storedProject);
    cleanupProjects.add(storedProject);
    return storedProject;
  }

  private Client createClient(Client client) throws Exception {
    List<Client> clients = clientStorage.getClients();
    assertNotNull(clients);
    Client storedClient = clientStorage.createClient(client);
    assertNotNull(storedClient);
    cleanupClients.add(storedClient);
    return storedClient;
  }

}
