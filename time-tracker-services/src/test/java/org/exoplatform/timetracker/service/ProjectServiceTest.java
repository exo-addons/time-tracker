package org.exoplatform.timetracker.service;

import junit.framework.TestCase;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Project;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProjectServiceTest extends TestCase {

    ProjectService projectService;
    ClientService clientService;
    private PortalContainer   container;
    List<Project> tearDownProjects = new ArrayList<>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        container = PortalContainer.getInstance();
        projectService = CommonsUtils.getService(ProjectService.class);
        clientService = CommonsUtils.getService(ClientService.class);
        begin();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for(Project project : tearDownProjects) {
            projectService.deleteProject(project.getId(), "root");
        }
        end();
    }

    private void begin() {
        RequestLifeCycle.begin(container);
    }

    private void end() {
        RequestLifeCycle.end();
    }

    public void testCreateProject() {
        Project project = null;
        try {
            try {
                project = projectService.createProject(project);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Expected
            }
            assertNull(project);
            project = createProject(1).get(0);
            assertNotNull(project);
            assertEquals("PROJ0", project.getCode());
        } catch (Exception e) {
            fail("Failed to create a project");
        }
    }

    public void testUpdateProject() {
        try {
            Project project = null;
            try {
                project = projectService.updateProject(project, "root");
            } catch (Exception e) {
                // Expected
            }
            project = new Project(null, "PROJX", "Project not saved yet", null);
            try {
                project = projectService.updateProject(project, "root");
            } catch (EntityNotFoundException e) {
                //Expected
            }
            project = new Project(5l, "PROJX", "Project not saved yet", null);
            try {
                project = projectService.updateProject(project, "root");
            } catch (EntityNotFoundException e) {
                //Expected
            }
            try {
                project = createProject(1).get(0);
            } catch (Exception e) {
                fail("Exception when create a project");
            }
            project.setCode("Updated Code");

            try {
                project = projectService.updateProject(project, null);
            } catch (IllegalArgumentException e) {
                //Expected username should not be null
            }
            project = projectService.updateProject(project, "root");
            assertEquals("Updated Code", project.getCode());
        } catch (Exception e) {
            fail("Problem running testUpdateProject : " + e.getMessage());
        }
    }

    public void testDeleteProject() {
        try {
            try {
                projectService.deleteProject(-1L, "root");
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                projectService.deleteProject(null, "root");
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                projectService.deleteProject(1L, null);
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                projectService.deleteProject(10L, "root");
            } catch (EntityNotFoundException e) {
                // Expected project should exist in DB
            }
            List<Project> projects = createProject(3);
            assertEquals(3, projectService.getProjectsList().size());
            tearDownProjects.remove(projects.get(0));
            projectService.deleteProject(projects.get(0).getId(), "root");
            assertEquals(2, projectService.getProjectsList().size());
        } catch (Exception e) {
            fail("Problem running testDeleteProject : " + e.getMessage());
        }
    }

    public void testGetProjectsList() {
    }

    private List<Project> createProject(long index) throws Exception{
        List<Project> projects = new ArrayList<>();
        Client client = new Client(null, "code", "label");
        client = clientService.createClient(client);
        for(int i = 0; i < index; i++) {
            Project project = new Project(null, "PROJ" + i, "Project Number " + i, client);
            project = projectService.createProject(project);
            projects.add(project);
            tearDownProjects.add(project);
        }
        return projects;
    }
}