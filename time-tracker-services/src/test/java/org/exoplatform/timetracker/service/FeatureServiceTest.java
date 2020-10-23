package org.exoplatform.timetracker.service;

import junit.framework.TestCase;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.storage.FeatureStorage;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FeatureServiceTest extends TestCase {

  private FeatureService featureService;

  private FeatureStorage featureStorage;


  @Before
  public void setUp() throws Exception {
    featureStorage = mock(FeatureStorage.class);
    featureService = new FeatureService(featureStorage);
  }

  public void testCreateFeature() throws Exception {
    // Given
    Feature feature = null;
    Feature newFeature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
    when(featureStorage.createFeature(any())).thenReturn(newFeature);

    // When
    feature = featureService.createFeature(newFeature);

    // Then
    assertNotNull(feature);
    verify(featureStorage, times(1)).createFeature(any());
  }

  public void testUpdateFeature() throws Exception {
    // Given
    Feature newfeatureUpdated = null;
    Feature feature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
    Feature featureUpdated = new Feature(1l,"testCodeUpdated","testLabelUpdated","testSpecUpdated","testExoUpdated");
    when(featureStorage.getFeatureById(anyLong())).thenReturn(feature);
    when(featureStorage.updateFeature(feature)).thenReturn(featureUpdated);

    // When
    newfeatureUpdated = featureService.updateFeature(feature,"root");

    // Then
    assertEquals(newfeatureUpdated.getCode(),"testCodeUpdated");
    assertEquals(newfeatureUpdated.getLabel(),"testLabelUpdated");
    assertEquals(newfeatureUpdated.getSpec(),"testSpecUpdated");
    assertEquals(newfeatureUpdated.getExo(),"testExoUpdated");
    assertEquals((long) newfeatureUpdated.getId(),1l);
    verify(featureStorage, times(1)).updateFeature(any());
    verify(featureStorage, times(1)).getFeatureById(anyLong());

  }

  public void testDeleteFeature() throws Exception  {
    // Given
    Feature feature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
    doNothing().when(featureStorage).deleteFeature(feature.getId());
    when(featureStorage.getFeatureById(feature.getId())).thenReturn(feature);

    // When
    featureService.deleteFeature(feature.getId(),"root");

    // Then
    verify(featureStorage, times(1)).deleteFeature(anyLong());
    verify(featureStorage, times(1)).getFeatureById(anyLong());
  }

  public void testGetFeaturesList() {
    // Given
    Feature feature = new Feature(1l,"testCode","testLabel","testSpec","testExo");
    Feature feature1 = new Feature(2l,"testCode1","testLabel1","testSpec1","testExo1");
    Feature feature2 = new Feature(3l,"testCode2","testLabel2","testSpec2","testExo2");
    Feature feature3 = new Feature(4l,"testCode3","testLabel3","testSpec3","testExo3");
    List<Feature> features = new ArrayList<>();
    features.add(feature);
    features.add(feature1);
    features.add(feature2);
    features.add(feature3);
    when(featureStorage.getFeatures()).thenReturn(features);

    // When
    List<Feature> featuresList = featureStorage.getFeatures();

    // Then
    assertEquals(4, featuresList.size());
    verify(featureStorage, times(1)).getFeatures();
  }
}