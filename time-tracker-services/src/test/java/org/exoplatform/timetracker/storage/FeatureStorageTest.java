
package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.entity.FeatureEntity;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

public class FeatureStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateFeature() throws Exception {
    Feature feature = createFeatureLocal();
    assertNotNull(feature);
    Feature newFeature = createFeature(feature);
    assertNotNull(newFeature);
    // Throw
    assertThrows(IllegalArgumentException.class, () -> featureStorage.createFeature(null));
  }

  @Test
  public void testUpdateFeature() throws Exception {
    Feature feature = createFeatureLocal();
    Feature newFeature = createFeature(feature);
    newFeature.setDisplayLabel("testUpdate5");
    Feature featureNotFound = featureStorage.getFeatures().get(0);
    featureNotFound.setId(5l);

    Feature newFeatureUpdated = featureStorage.updateFeature(newFeature);

    // Throw
    assertNotNull(newFeatureUpdated);
    assertThrows(IllegalArgumentException.class, () -> featureStorage.updateFeature(null));
    assertThrows(EntityNotFoundException.class, () -> featureStorage.updateFeature(featureNotFound));
  }

  @Test
  public void testDeleteFeature() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Feature feature = createFeatureLocal();
    Feature newFeature = createFeature(feature);
    Long featureId = newFeature.getId();
    Long featureIdNull = 0l;
    Long featureIdNotFound = 5l;
    cleanupClients.remove(clientNew);
    cleanupFeatures.remove(newFeature);
    featureStorage.deleteFeature(featureId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> featureStorage.deleteFeature(featureIdNull));
    assertThrows(EntityNotFoundException.class, () -> featureStorage.deleteFeature(featureIdNotFound));

  }

  @Test
  public void testGetFeatureById() throws Exception {
    Feature feature = createFeatureLocal();
    Feature featureNew = createFeature(feature);
    Long featureId = featureNew.getId();
    Long featureIdNull = 0l;

    Feature newFeature = featureStorage.getFeatureById(featureId);

    // Throw
    assertNotNull(newFeature);
    assertThrows(IllegalArgumentException.class, () -> featureStorage.getFeatureById(featureIdNull));

  }

  @Test
  public void testGetFeatures() throws Exception {
    Feature feature = createFeatureLocal();
    createFeature(feature);
    List<Feature> features = featureStorage.getFeatures();
    assertNotNull(features);
    assertTrue(!features.isEmpty());

  }

  private Feature createFeatureLocal() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    Project project = new Project(null, "test", "test", clientNew);
    Project newProject = createProject(project);
    Feature feature = new Feature(null, "test", "test","test", "test", newProject);
    return feature;
  }

  @Test
  public void testCountFeatures() throws Exception {
    Feature feature = createFeatureLocal();
    createFeature(feature);
    Long countFeature = featureStorage.countFeatures();
    assertNotNull(countFeature);
  }

  @Test
  public void toDTO() {
    FeatureEntity featureEntityNull = null;
    Feature featureDTO = featureStorage.toDTO(featureEntityNull);
    assertNull(featureDTO);
  }

  @Test
  public void toEntity() {
    Feature featureNull = null;
    FeatureEntity featureEntity = featureStorage.toEntity(featureNull);
    assertNull(featureEntity);
  }

  private Feature createFeature(Feature feature) throws Exception {
    List<Feature> features = featureStorage.getFeatures();
    assertNotNull(features);
    Feature storedFeature = featureStorage.createFeature(feature);
    assertNotNull(storedFeature);
    cleanupFeatures.add(storedFeature);
    return storedFeature;
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