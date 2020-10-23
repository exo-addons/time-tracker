package org.exoplatform.timetracker.storage;

import junit.framework.TestCase;
import org.exoplatform.timetracker.dao.FeatureDAO;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.entity.FeatureEntity;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FeatureStorageTest extends TestCase {

  private FeatureDAO featureDAO;

  private FeatureStorage featureStorage;

  @Before
  public void setUp() throws Exception {
    featureDAO = mock(FeatureDAO.class);
    featureStorage = new FeatureStorage(featureDAO);
  }

  public void testCreateFeature() throws Exception {
    // Given
     FeatureEntity featureEntity = new FeatureEntity(1l,"testCode","testLabel","testSpec","testExo");
     Feature newFeature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
     when(featureDAO.create(featureEntity)).thenReturn(featureEntity);
     Feature feature = null;

    // When
     feature = featureStorage.createFeature(newFeature);

    // Then
     assertNotNull(feature);
     verify(featureDAO, times(1)).create(any());
  }

  public void testUpdateFeature() throws Exception {
    // Given
    FeatureEntity featureEntity = new FeatureEntity(1l,"testCode","testLabel","testSpec","testExo");
    FeatureEntity featureEntityUpdated = new FeatureEntity(1l,"testCodeUpdate","testLabelUpdate","testSpecUpdate","testExoUpdate");
    Feature feature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
    Feature newFeatureUpdated = null;
    when(featureDAO.find(feature.getId())).thenReturn(featureEntity);
    when(featureDAO.update(featureEntity)).thenReturn(featureEntityUpdated);

    // When
    newFeatureUpdated = featureStorage.updateFeature(feature);

    // Then
    assertEquals((long)newFeatureUpdated.getId(),1l);
    assertEquals(newFeatureUpdated.getLabel(),"testLabelUpdate");
    assertEquals(newFeatureUpdated.getSpec(),"testSpecUpdate");
    assertEquals(newFeatureUpdated.getExo(),"testExoUpdate");
    assertEquals(newFeatureUpdated.getCode(),"testCodeUpdate");
    verify(featureDAO, times(1)).find(anyLong());
    verify(featureDAO, times(1)).update(any());
  }

  public void testDeleteFeature() {
    // Given
    FeatureEntity featureEntity = new FeatureEntity(1l,"testCode","testLabel","testSpec","testExo");
    Feature feature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
    when(featureDAO.find(feature.getId())).thenReturn(featureEntity);
    doNothing().when(featureDAO).delete(featureEntity);

    // When
    featureStorage.deleteFeature(feature.getId());

    // Then
    verify(featureDAO, times(1)).find(anyLong());
    verify(featureDAO, times(1)).delete(any());
  }

  public void testGetFeatures() {
    // Given
    FeatureEntity featureEntity = new FeatureEntity(1l,"testCode1","testLabel1","testSpec1","testExo1");
    FeatureEntity featureEntity1 = new FeatureEntity(2l,"testCode2","testLabel2","testSpec2","testExo2");
    FeatureEntity featureEntity2 = new FeatureEntity(3l,"testCode3","testLabel3","testSpec3","testExo3");
    FeatureEntity featureEntity3 = new FeatureEntity(4l,"testCode4","testLabel4","testSpec4","testExo4");
    List<FeatureEntity> features = new ArrayList<>();
    features.add(featureEntity);
    features.add(featureEntity1);
    features.add(featureEntity2);
    features.add(featureEntity3);
    when(featureDAO.findAll()).thenReturn(features);

    // When
    List<Feature> clientsList = featureStorage.getFeatures();

    // Then
    assertEquals(4,clientsList.size());
    verify(featureDAO, times(1)).findAll();
  }
  public void testGetFeatureById(){
    // Given
    FeatureEntity featureEntity = new FeatureEntity(1l,"testCode1","testLabel1","testSpec1","testExo1");
    when(featureDAO.find(eq(1l))).thenReturn(featureEntity);
    Feature notExistingFeature = featureStorage.getFeatureById(2l);
    assertNull(notExistingFeature);
    verify(featureDAO, times(1)).find(anyLong());

    // When
    Feature feature = featureStorage.getFeatureById(1l);

    // Then
    assertNotNull(feature);
    verify(featureDAO, times(2)).find(anyLong());
  }
}