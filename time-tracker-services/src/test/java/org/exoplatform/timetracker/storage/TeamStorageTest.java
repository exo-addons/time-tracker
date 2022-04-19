package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.junit.Test;

import java.util.List;

public class TeamStorageTest extends BaseTimeTrackerTest {
  @Test
  public void testCreateTeam() throws Exception {
    Team team = new Team(null, "test06", "test06");
    // When
    Team teamNew = createTeam(team);

    // Then
    assertNotNull(teamNew);
    assertEquals(team.getDescription(), teamNew.getDescription());
    assertEquals(team.getName(), teamNew.getName());
    // Throw
    assertThrows(IllegalArgumentException.class, () -> teamStorage.createTeam(null));
  }

//  @Test
//  public void testUpdateTeam() throws Exception {
//    Team team = new Team(null, "test05", "test05");
//    Team storedTeam = createTeam(team);
//    storedTeam.setDescription("test2");
//    storedTeam.setName("test2 name");
//    Team updatedTeam = teamStorage.updateTeam(storedTeam);
//    assertNotNull(updatedTeam);
//    assertEquals("test2", updatedTeam.getDescription());
//    assertEquals("test2 name", updatedTeam.getName());
//    assertThrows(IllegalArgumentException.class, () -> teamStorage.updateTeam(null));
//    Team updatedTeamFromDB = teamStorage.getTeamById(updatedTeam.getId());
//    assertNotNull(updatedTeamFromDB);
//    assertEquals("test2", updatedTeamFromDB.getDescription());
//    assertEquals("test2 name", updatedTeamFromDB.getName());
//  }

  @Test
  public void testDeleteTeam() throws Exception {
    Team team = new Team(null, "test04", "test04");
    Team storedTeam = createTeam(team);
    String teamId = storedTeam.getId();
    teamStorage.deleteTeam(teamId);
    cleanupTeams.remove(storedTeam);
    List<Team> teams = teamStorage.getTeams();
    assertNotNull(teams);
    assertTrue(teams.isEmpty());
  }

  @Test
  public void testGetTeamById() throws Exception {
    Team team = new Team(null, "test03", "test03");
    Team storedTeam = createTeam(team);
    String teamId = storedTeam.getId();
    Team newTeamUpdated = teamStorage.getTeamById(teamId);
    assertNotNull(newTeamUpdated);
    assertEquals(storedTeam.getId(), newTeamUpdated.getId());
    assertEquals(storedTeam.getDescription(), newTeamUpdated.getDescription());
    assertEquals(storedTeam.getName(), newTeamUpdated.getName());
  }

  @Test
  public void testGetTeams() throws Exception {
    Team team = new Team(null, "test02", "test02");
    createTeam(team);
    List<Team> teams = teamStorage.getTeams();
    assertNotNull(teams);
    assertTrue(!teams.isEmpty());
    assertEquals(1, teams.size());
  }
  
  private Team createTeam(Team team) throws Exception {
    List<Team> teams = teamStorage.getTeams();
    assertNotNull(teams);
    assertTrue(teams.isEmpty());
    Team storedTeam = teamStorage.createTeam(team);
    assertNotNull(storedTeam);
    cleanupTeams.add(storedTeam);
    return storedTeam;
  }

}
