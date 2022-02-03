package org.exoplatform.timetracker.service;

import junit.framework.TestCase;
import org.exoplatform.timetracker.dto.Filter;
import org.exoplatform.timetracker.dto.FilterField;
import org.exoplatform.timetracker.dto.FilterModel;
import org.exoplatform.timetracker.storage.FilterStorage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FilterServiceTest extends TestCase {
  private FilterService filterService;
  private FilterStorage filterStorage;


  @Before
  public void setUp() throws Exception {
    filterStorage = mock(FilterStorage.class);
    filterService = new FilterService(filterStorage);
  }

  public void testCreateFilter() throws Exception {
    // Given
    Filter filter =new Filter(1l,"testName","root");
    FilterField filterField = new FilterField(1l,"test",new String[]{"test"},filter);
    List<FilterField> filterFields = new ArrayList<FilterField>();
    filterFields.add(filterField);
    FilterModel filterModel = new FilterModel(filter,filterFields);
    when(filterStorage.createFilter(filter)).thenReturn(filter);
    when(filterStorage.createFilterField(filterField)).thenReturn(filterField);
    FilterModel newFilterModel = null;

    // When
    newFilterModel = filterService.createFilter(filterModel,"root");

    // Then
    assertNotNull(newFilterModel);
    verify(filterStorage, times(1)).createFilter(any());
    verify(filterStorage, times(1)).createFilterField(any());
  }

  public void testDeleteFilter() throws IllegalAccessException {
    // Given
    Filter storedFilter =new Filter(1l,"testName","root");
    when(filterStorage.getFilterById(1l)).thenReturn(storedFilter);
    doNothing().when(filterStorage).deleteAllFilterFieldsByFilter(anyLong());
    doNothing().when(filterStorage).deleteFilter(anyLong());

    // When
    filterService.deleteFilter(1l);

    // Then
    verify(filterStorage, times(1)).getFilterById(anyLong());
    verify(filterStorage, times(1)).deleteFilter(anyLong());
    verify(filterStorage, times(1)).deleteAllFilterFieldsByFilter(anyLong());
  }

  public void testGetFiltersList() {
    // Given
    JSONArray filterModels = new JSONArray();
    Filter filter =new Filter(1l,"testName","root");
    Filter filter1 =new Filter(2l,"testName1","root");
    Filter filter2 =new Filter(3l,"testName2","root");
    Filter filter3 =new Filter(4l,"testName3","root");
    List<Filter> filters = new ArrayList<Filter>();
    filters.add(filter);
    filters.add(filter1);
    filters.add(filter2);
    filters.add(filter3);
    when(filterStorage.getFiltersByUserName("root")).thenReturn(filters);

    // When
    filterModels = filterService.getFiltersList("root");

    // Then
    assertEquals(filterModels.length(), 4);
    verify(filterStorage, times(1)).getFiltersByUserName(any());
  }

  public void testGetFields() {
    // Given
    JSONObject fieldsJson = new JSONObject();
    Filter filter =new Filter(1l,"testName","root");
    Filter filter1 =new Filter(2l,"testName1","root");
    Filter filter2 =new Filter(3l,"testName2","root");
    FilterField filterField = new FilterField(1l,"test",new String[]{"test"},filter);
    FilterField filterField1 = new FilterField(2l,"test1",new String[]{"test1"},filter1);
    FilterField filterField2 = new FilterField(3l,"test2",new String[]{"test2"},filter2);
    List<FilterField> fieldsList = new ArrayList<FilterField>();
    fieldsList.add(filterField);
    fieldsList.add(filterField1);
    fieldsList.add(filterField2);
    when(filterStorage.getFilterFieldsByFilter(1l)).thenReturn(fieldsList);

    // When
    fieldsJson = filterService.getFields(1l);

    // Then
    assertNotNull(fieldsJson);
    verify(filterStorage, times(1)).getFilterFieldsByFilter(anyLong());
  }
}