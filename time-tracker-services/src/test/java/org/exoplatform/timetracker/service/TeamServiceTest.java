package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.storage.TeamStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import junit.framework.TestCase;

public class TeamServiceTest extends TestCase {

	private TeamService teamService;

	private TeamStorage teamStorage;

	@Before
	public void setUp() throws Exception {
		teamStorage = mock(TeamStorage.class);
		teamService = new TeamService(teamStorage);
	}

	@Test
	public void testCreateTeam() throws Exception {
		// Given
		Team team = null;
		Team newTeam = new Team("1", "testCode", "testLabel");
		when(teamStorage.createTeam(newTeam)).thenReturn(newTeam);

		// When
		team = teamService.createTeam(newTeam);

		// Then
		verify(teamStorage, times(1)).createTeam(any());

		// Throw
		try {
			team = teamService.createTeam(null);
			fail("Team is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, Team is mandatory
		}
	}

	@Test
	public void testUpdateTeam() throws Exception {
		// Given
		Team newTeamUpdated = null;
		Team team = new Team("1", "testCode", "testLabel");
		Team teamUpdated = new Team("1", "testCodeUpdated", "testLabelUpdated");
		when(teamStorage.getTeamById(any())).thenReturn(team);
		when(teamStorage.updateTeam(team)).thenReturn(teamUpdated);

		// When
		newTeamUpdated = teamService.updateTeam(team);

		// Then
		assertEquals(newTeamUpdated.getId(), "1");
		verify(teamStorage, times(1)).updateTeam(any());
		verify(teamStorage, times(1)).getTeamById(any());

		// Throw
		try {
			newTeamUpdated = teamService.updateTeam(null);
			fail("Activity is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}

		try {
			String teamId = "1";
			when(teamStorage.getTeamById(teamId)).thenReturn(null);
			team = teamService.updateTeam(team);
			fail("Activity with id '1l' wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newTeamUpdated = team;
			newTeamUpdated.setId(null);
			newTeamUpdated = teamService.updateTeam(newTeamUpdated);
			fail("Activity with null id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}

	}

	@Test
	public void testDeleteTeam() throws Exception {
		// Given
		Team team = new Team("1", "testCode", "testLabel");
		doNothing().when(teamStorage).deleteTeam(team.getId());
		when(teamStorage.getTeamById(team.getId())).thenReturn(team);

		// When
		teamService.deleteTeam(team.getId());

		// Then
		verify(teamStorage, times(1)).deleteTeam(any());
		verify(teamStorage, times(1)).getTeamById(any());

		// Throw
		try {
			String teamId = team.getId();
			when(teamStorage.getTeamById(teamId)).thenReturn(null);
			teamService.deleteTeam(teamId);
			fail("Activity with id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, Team remoteId is mandatory

		}
	}

	@Test
	public void testGetTeamsList() throws Exception {
		// Given
		Team team = new Team("1", "testCode", "testLabel");
		Team team1 = new Team("2", "testCode1", "testLabel1");
		Team team2 = new Team("3", "testCode2", "testLabel2");
		Team team3 = new Team("4", "testCode3", "testLabel3");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		teams.add(team1);
		teams.add(team2);
		teams.add(team3);
		when(teamStorage.getTeamsByUser("root")).thenReturn(teams);

		// When
		List<Team> teamsList = teamService.getTeamsList("root");

		// Then
		assertEquals(4, teamsList.size());
		verify(teamStorage, times(1)).getTeamsByUser(any());
	}

	@Test
	public void testCreateTeamMember() throws Exception {
		// Given
		List<Team> newTeamsList= new ArrayList<>();
		Team team = new Team("1", "testCode", "testLabel");
		TeamMember newTeamMember = new TeamMember("1", "root", "testFullName", team);
		List<Team> teamsList= new ArrayList<>();
		teamsList.add(team);
		Map<String, List<Team>> teams = new HashMap<>();
		teams.put(newTeamMember.getUserName(),teamsList);
		when(teamStorage.getTeamsByUser(any())).thenReturn(teamsList);
		when(teamStorage.getMemberByTeamUserAndRole(any(), any(), any())).thenReturn(null);
		doNothing().when(teamStorage).createTeamMember(newTeamMember);

		// When
		teamService.createTeamMember(newTeamMember);
		
		// Then
		assertNotNull(teams.get(newTeamMember.getUserName()));
		verify(teamStorage, times(1)).createTeamMember(any());
		//verify(teamStorage, times(1)).getTeamsByUser(any());

		// Throw
		try {
			teamService.createTeamMember(null);
			fail("TeamMember is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, TeamMember is mandatory
		}
		try {
			when(teamStorage.getMemberByTeamUserAndRole("true", "true", "true")).thenReturn(newTeamMember);
			teamService.createTeamMember(newTeamMember);
			fail("TeamMember Already exist");
		} catch (EntityExistsException e) {
			// Expected, TeamMember Already exist
		}
	}

	@Test
	public void testDeleteTeamMember() throws Exception {
		// Given
		Team team = new Team("1", "testCode", "testLabel");
		TeamMember teamMember = new TeamMember("1", "testCode", "testLabel", team);
		doNothing().when(teamStorage).deleteTeamMember(teamMember.getId());
		when(teamStorage.getTeamMemberById(teamMember.getId())).thenReturn(teamMember);

		// When
		teamService.deleteTeamMember(teamMember.getId());

		// Then
		verify(teamStorage, times(1)).deleteTeamMember(any());
		verify(teamStorage, times(1)).getTeamMemberById(any());

		// Throw
		try {
			String teamMemberId = teamMember.getId();
			when(teamStorage.getTeamMemberById(teamMemberId)).thenReturn(null);
			teamService.deleteTeamMember(teamMemberId);
			fail("Activity with id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, TeamMember remoteId is mandatory

		}
	}

}
