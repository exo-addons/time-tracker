package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Filter;
import org.exoplatform.timetracker.dto.FilterField;
import org.exoplatform.timetracker.entity.FilterFieldEntity;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

public class FilterStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateFilterField() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    assertNotNull(filterField);
    FilterField newFilterField = createFilterField(filterField);
    assertNotNull(newFilterField);
    // Throw
    assertThrows(IllegalArgumentException.class, () -> filterStorage.createFilterField(null));
  }

  @Test
  public void testUpdateFilterField() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    FilterField newFilterField = createFilterField(filterField);
    newFilterField.setName("testUpdate5");
    FilterField filterFieldNotFound = filterStorage.getFilterFields().get(0);
    filterFieldNotFound.setId(5l);

    FilterField newFilterFieldUpdated = filterStorage.updateFilterField(newFilterField);

    // Throw
    assertNotNull(newFilterFieldUpdated);
    assertThrows(IllegalArgumentException.class, () -> filterStorage.updateFilterField(null));
    assertThrows(EntityNotFoundException.class, () -> filterStorage.updateFilterField(filterFieldNotFound));
  }

  @Test
  public void testDeleteFilterField() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    FilterField newFilterField = createFilterField(filterField);
    Long filterFieldId = newFilterField.getId();
    Long filterFieldIdNull = 0l;
    Long filterFieldIdNotFound = 5l;
    cleanupFilterFields.remove(newFilterField);
    filterStorage.deleteFilterField(filterFieldId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> filterStorage.deleteFilterField(filterFieldIdNull));
    assertThrows(EntityNotFoundException.class, () -> filterStorage.deleteFilterField(filterFieldIdNotFound));

  }

  @Test
  public void testDeleteAllFilterFieldsByFilter() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    FilterField newFilterField = createFilterField(filterField);
    Long filterId = filterNew.getId();
    Long filterIdNull = 0l;
    Long filterIdNotFound = 5000l;
    cleanupFilterFields.remove(newFilterField);
    filterStorage.deleteAllFilterFieldsByFilter(filterId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> filterStorage.deleteAllFilterFieldsByFilter(filterIdNull));

  }

  @Test
  public void testGetFilterFieldById() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    FilterField filterFieldNew = createFilterField(filterField);
    Long filterFieldId = filterFieldNew.getId();
    Long filterFieldIdNull = 0l;
    FilterField newFilterField = filterStorage.getFilterFieldById(filterFieldId);

    // Throw
    assertNotNull(newFilterField);
    assertThrows(IllegalArgumentException.class, () -> filterStorage.getFilterFieldById(filterFieldIdNull));
  }
  
  @Test
  public void getFilterFieldsByFilter() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    FilterField filterFieldNew = createFilterField(filterField);
    Long filterId = filterNew.getId();
    Long filterIdNull = 0l;
    List<FilterField> newFilterField = filterStorage.getFilterFieldsByFilter(filterId);

    assertNotNull(newFilterField);
    assertTrue(!newFilterField.isEmpty());
    assertEquals(newFilterField.get(0).getId(),filterFieldNew.getId());
    assertThrows(IllegalArgumentException.class, () -> filterStorage.getFilterFieldById(filterIdNull));
  }

  @Test
  public void testGetFilterFields() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    createFilterField(filterField);
    List<FilterField> filterFields = filterStorage.getFilterFields();
    assertNotNull(filterFields);
    assertTrue(!filterFields.isEmpty());

  }

  @Test
  public void testCountFilterFields() throws Exception {
    Filter filter = new Filter(null, "test", "test");
    Filter filterNew = createFilter(filter);
    FilterField filterField = new FilterField(null, "test", new String[] { "test" }, filterNew);
    createFilterField(filterField);
    Long countFilterField = filterStorage.countFilterFields();
    assertNotNull(countFilterField);
  }

  @Test
  public void toDTO() {
    FilterFieldEntity filterFieldEntityNull = null;
    FilterField filterFieldDTO = filterStorage.toDTO(filterFieldEntityNull);
    assertNull(filterFieldDTO);
  }

  @Test
  public void toEntity() {
    FilterField filterFieldNull = null;
    FilterFieldEntity filterFieldEntity = filterStorage.toEntity(filterFieldNull);
    assertNull(filterFieldEntity);
  }

  @Test
  public void testCreateFilter() throws Exception {
    Filter filter = new Filter(null, "test17", "test17");
    Filter filterNew = createFilter(filter);
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(!filters.isEmpty());
    assertNotNull(filterNew);
    assertEquals(filter.getName(), filterNew.getName());
    assertEquals(filter.getUserName(), filterNew.getUserName());
    assertThrows(IllegalArgumentException.class, () -> filterStorage.createFilter(null));
  }

  @Test
  public void testUpdateFilter() throws Exception {
    Filter filter = new Filter(null, "test19", "test19");
    Filter storedFilter = createFilter(filter);
    storedFilter.setName("test2 label");
    Filter updatedFilter = filterStorage.updateFilter(storedFilter);
    assertNotNull(updatedFilter);
    assertEquals("test2 label", updatedFilter.getName());
    assertThrows(IllegalArgumentException.class, () -> filterStorage.updateFilter(null));
    Filter updatedFilterFromDB = filterStorage.getFilterById(updatedFilter.getId());
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(!filters.isEmpty());
    assertNotNull(updatedFilterFromDB);
    assertEquals("test2 label", updatedFilterFromDB.getName());
  }

  @Test
  public void testDeleteFilter() throws Exception {
    Filter filter = new Filter(null, "test12", "test12");
    Filter storedFilter = createFilter(filter);
    Long filterId = storedFilter.getId();
    cleanupFilters.remove(storedFilter);
    filterStorage.deleteFilter(filterId);
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(filters.isEmpty());
    assertThrows(IllegalArgumentException.class, () -> filterStorage.deleteFilter(0l));
    assertThrows(EntityNotFoundException.class, () -> filterStorage.deleteFilter(5000l));
  }

  @Test
  public void testGetFilterById() throws Exception {
    Filter filter = new Filter(null, "test11", "test11");
    Filter storedFilter = createFilter(filter);
    Long filterId = storedFilter.getId();
    Long filterIdNull = 0l;
    Filter newFilterUpdated = filterStorage.getFilterById(filterId);
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(!filters.isEmpty());
    assertNotNull(newFilterUpdated);
    assertEquals(storedFilter.getId(), newFilterUpdated.getId());
    assertEquals(storedFilter.getName(), newFilterUpdated.getName());
    assertThrows(IllegalArgumentException.class, () -> filterStorage.getFilterById(filterIdNull));
  }

  @Test
  public void testGetFiltersByUserName() throws Exception {
    Filter filter = new Filter(null, "test11", "test11");
    Filter storedFilter = createFilter(filter);
    String filterUserName = storedFilter.getUserName();
    Long filterIdNull = 0l;
    List<Filter> newFilterUpdated = filterStorage.getFiltersByUserName(filterUserName);
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(!filters.isEmpty());
    assertNotNull(newFilterUpdated);
    assertThrows(IllegalArgumentException.class, () -> filterStorage.getFilterById(filterIdNull));
  }

  @Test
  public void testGetFilters() throws Exception {
    Filter filter = new Filter(null, "test15", "test15");
    createFilter(filter);
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(!filters.isEmpty());
  }

  @Test
  public void testCountFilters() throws Exception {
    Filter filter = new Filter(null, "test7", "test7");
    createFilter(filter);
    Long countFilters = filterStorage.countFilters();
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    assertTrue(!filters.isEmpty());
    assertTrue(countFilters == 1l);
  }

  private FilterField createFilterField(FilterField filterField) throws Exception {
    List<FilterField> filterFields = filterStorage.getFilterFields();
    assertNotNull(filterFields);
    FilterField storedFilterField = filterStorage.createFilterField(filterField);
    assertNotNull(storedFilterField);
    cleanupFilterFields.add(storedFilterField);
    return storedFilterField;
  }

  private Filter createFilter(Filter filter) throws Exception {
    List<Filter> filters = filterStorage.getFilters();
    assertNotNull(filters);
    Filter storedFilter = filterStorage.createFilter(filter);
    assertNotNull(storedFilter);
    cleanupFilters.add(storedFilter);
    return storedFilter;
  }
}
