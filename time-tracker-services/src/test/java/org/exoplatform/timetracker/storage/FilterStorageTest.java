package org.exoplatform.timetracker.storage;

import junit.framework.TestCase;
import org.exoplatform.timetracker.dao.FilterDAO;
import org.exoplatform.timetracker.dao.FilterFieldDAO;
import org.exoplatform.timetracker.dto.Filter;
import org.exoplatform.timetracker.dto.FilterField;
import org.exoplatform.timetracker.entity.FilterEntity;
import org.exoplatform.timetracker.entity.FilterFieldEntity;
import org.junit.Before;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class FilterStorageTest extends TestCase {

  private  FilterDAO filterDAO;

  private  FilterFieldDAO filterFieldDAO;

  private FilterStorage filterStorage;

  @Before
  public void setUp() throws Exception {
    filterDAO = mock(FilterDAO.class);
    filterFieldDAO = mock(FilterFieldDAO.class);
    filterStorage = new FilterStorage(filterDAO,filterFieldDAO);
  }

  public void testCreateFilter() throws Exception {
    //Given
    Filter newFilterCreated = null;
    Filter filter =new Filter(1l,"testName","root");
    FilterEntity filterEntity =new FilterEntity(1l,"testName","root");
    when(filterDAO.create(filterEntity)).thenReturn(filterEntity);

    //When
    newFilterCreated = filterStorage.createFilter(filter);

    //Then
    assertNotNull(newFilterCreated);
    verify(filterDAO, times(1)).create(any());
  }

  public void testUpdateFilter() throws Exception {
      //Given
      Filter filterUpdated = null;
      Filter filter =new Filter(1l,"testName","root");
      FilterEntity filterEntity =new FilterEntity(1l,"testName","root");
      FilterEntity filterEntityUpdated =new FilterEntity(1l,"testNameUpdated","root");
      when(filterDAO.find(filter.getId())).thenReturn(filterEntity);
      when(filterDAO.update(filterEntity)).thenReturn(filterEntityUpdated);

      //When
      filterUpdated = filterStorage.updateFilter(filter);

      //Then
      assertNotNull(filterUpdated);
      assertEquals(filterUpdated.getName(),"testNameUpdated");
      assertEquals((long)filterUpdated.getId(),1l);
      verify(filterDAO, times(1)).find(anyLong());
      verify(filterDAO, times(1)).update(any());
  }

  public void testDeleteFilter() {
    //Given
    FilterEntity filterEntity =new FilterEntity(1l,"testName","root");
    when(filterDAO.find(1l)).thenReturn(filterEntity);
    doNothing().when(filterDAO).delete(filterEntity);


    //When
    filterStorage.deleteFilter(1l);

    //Then
    verify(filterDAO, times(1)).find(anyLong());
    verify(filterDAO, times(1)).delete(any());
  }

  public void testGetFilters() {
    //Given
    Filter filter = null;
    FilterEntity filterEntity =new FilterEntity(1l,"testName","root");
    when(filterDAO.find(1l)).thenReturn(filterEntity);

    //When
    filter = filterStorage.getFilterById(1l);


    //Then
    verify(filterDAO, times(1)).find(anyLong());
    assertNotNull(filter);
  }

  public void testCreateFilterField() throws Exception {
    //Given
    FilterField filterFieldCreated = null;
    FilterEntity filterEntity =new FilterEntity(1l,"testName","root");
    Filter filter =new Filter(1l,"testName","root");
    FilterField filterField = new FilterField(1l,"test","test",filter);
    FilterFieldEntity filterFieldEntity = new FilterFieldEntity(1l,"testName","testValue",filterEntity);
    when(filterFieldDAO.create(any())).thenReturn(filterFieldEntity);

    //When
    filterFieldCreated = filterStorage.createFilterField(filterField);

    //Then
    assertNotNull(filterFieldCreated);
    verify(filterFieldDAO, times(1)).create(any());
  }

  public void testDeleteFilterField() {
    //Given
    FilterEntity filterEntity =new FilterEntity(2l,"testName","root");
    FilterFieldEntity filterFieldEntity = new FilterFieldEntity(1l,"testName","testValue",filterEntity);
    when(filterFieldDAO.find(anyLong())).thenReturn(filterFieldEntity);
    doNothing().when(filterFieldDAO).delete(filterFieldEntity);

    //When
    filterStorage.deleteFilterField(1l);

    //Then
    verify(filterFieldDAO, times(1)).find(anyLong());
    verify(filterFieldDAO, times(1)).delete(any());
  }
}