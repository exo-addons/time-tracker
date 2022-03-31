package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.storage.FeatureStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import junit.framework.TestCase;

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
		Feature newFeature = new Feature(1l, "testCode", "testLabel", "testSpec", "testExo", null);
		when(featureStorage.createFeature(newFeature)).thenReturn(newFeature);

		// When
		feature = featureService.createFeature(newFeature);

		// Then
		verify(featureStorage, times(1)).createFeature(any());

		// Throw
		try {
			feature = featureService.createFeature(null);
			fail("Feature is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, feature is mandatory
		}
	}

	public void testUpdateFeature() throws Exception {
		// Given
		Feature newfeatureUpdated = null;
		Feature feature = new Feature(1l, "testCode", "testLabel", "testSpec", "testExo", null);
		Feature featureUpdated = new Feature(1l, "testCodeUpdated", "testLabelUpdated", "testSpecUpdated",
				"testExoUpdated", null);
		when(featureStorage.getFeatureById(anyLong())).thenReturn(feature);
		when(featureStorage.updateFeature(feature)).thenReturn(featureUpdated);

		// When
		newfeatureUpdated = featureService.updateFeature(feature, "root");

		// Then
		assertEquals(newfeatureUpdated.getCode(), "testCodeUpdated");
		assertEquals(newfeatureUpdated.getLabel(), "testLabelUpdated");
		assertEquals(newfeatureUpdated.getSpec(), "testSpecUpdated");
		assertEquals(newfeatureUpdated.getExo(), "testExoUpdated");
		assertEquals((long) newfeatureUpdated.getId(), 1l);
		verify(featureStorage, times(1)).updateFeature(any());
		verify(featureStorage, times(1)).getFeatureById(anyLong());

		// Throw
		try {
			newfeatureUpdated = featureService.updateFeature(null, "root");
			fail("Activity is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newfeatureUpdated = featureService.updateFeature(feature, null);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity userName is mandatory
		}
		try {
			Long featureId = 1l;
			when(featureStorage.getFeatureById(featureId)).thenReturn(null);
			feature = featureService.updateFeature(feature, "root");
			fail("Activity with id '1l' wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newfeatureUpdated = feature;
			newfeatureUpdated.setId(null);
			newfeatureUpdated = featureService.updateFeature(newfeatureUpdated, "root");
			fail("Activity with null id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}

	}

	public void testDeleteFeature() throws Exception {
		// Given
		Feature feature = new Feature(1l, "testCode", "testLabel", "testSpec", "testExo", null);
		doNothing().when(featureStorage).deleteFeature(feature.getId());
		when(featureStorage.getFeatureById(feature.getId())).thenReturn(feature);

		// When
		featureService.deleteFeature(feature.getId(), "root");

		// Then
		verify(featureStorage, times(1)).deleteFeature(anyLong());
		verify(featureStorage, times(1)).getFeatureById(anyLong());

		// Throw
		try {
			featureService.deleteFeature(feature.getId(), null);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, feature remoteId is mandatory
		}
		try {
			Long featureId = feature.getId();
			when(featureStorage.getFeatureById(featureId)).thenReturn(null);
			featureService.deleteFeature(featureId, "root");
			fail("Activity with id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, feature remoteId is mandatory

		}
		try {
			featureService.deleteFeature(null, "root");
			fail("ActivityId must be a positive integer");
		} catch (IllegalArgumentException e) {
			// Expected, feature remoteId is mandatory
		}
	}

	@Test
	public void testGetFeaturesList() {
		// Given
		Feature feature = new Feature(1l, "testCode", "testLabel", "testSpec", "testExo", null);
		Feature feature1 = new Feature(2l, "testCode1", "testLabel1", "testSpec1", "testExo1", null);
		Feature feature2 = new Feature(3l, "testCode2", "testLabel2", "testSpec2", "testExo2", null);
		Feature feature3 = new Feature(4l, "testCode3", "testLabel3", "testSpec3", "testExo3", null);
		List<Feature> features = new ArrayList<>();
		features.add(feature);
		features.add(feature1);
		features.add(feature2);
		features.add(feature3);
		when(featureStorage.getFeatures()).thenReturn(features);

		// When
		List<Feature> featuresList = featureService.getFeaturesList();

		// Then
		assertEquals(4, featuresList.size());
		verify(featureStorage, times(1)).getFeatures();
	}
}