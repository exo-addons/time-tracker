/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.exoplatform.timetracker.service;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.storage.TeamStorage;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * A Service to access and store Teams
 */
public class TeamService {

    private static final Log LOG = ExoLogger.getLogger(TeamService.class);

    private final TeamStorage teamStorage;

    public TeamService(TeamStorage teamStorage) {
        this.teamStorage = teamStorage;

    }

    /**
     * Create new Team that will be available for all users. If the Team
     * already exits an {@link EntityExistsException} will be thrown.
     *
     * @param team Team to create
     * @return stored {@link Team} in datasource
     * @throws Exception when Team already exists or an error occurs while
     *                   creating Team or its attached image
     */
    public Team createTeam(Team team) throws Exception {
        if (team == null) {
            throw new IllegalArgumentException("Team is mandatory");
        }
        return teamStorage.createTeam(team);

    }


    /**
     * Update an existing Project on datasource. If the Project doesn't exit an
     * {@link EntityNotFoundException} will be thrown.
     *
     * @param team dto to update on store
     * @return stored {@link Team} in datasource
     * @throws Exception when {@link EntityNotFoundException} is thrown or an error
     *           occurs while saving Project
     */
    public Team updateTeam(Team team) throws Exception {
        if (team == null) {
            throw new IllegalArgumentException("Team is mandatory");
        }
        Long teamId = team.getId();
        if (teamId == null) {
            throw new EntityNotFoundException("Team with null id wasn't found");
        }
        Team storedProject = teamStorage.getTeamById(teamId);
        if (storedProject == null) {
            throw new EntityNotFoundException("Team with id " + teamId + " wasn't found");
        }
        return teamStorage.updateTeam(team);
    }



    /**
     * Delete Team identified by its id and check if username has permission to
     * delete it.
     *
     * @param teamId technical identifier of Team
     * @throws EntityNotFoundException if Team wasn't found
     * @throws IllegalAccessException  if user is not allowed to delete Team
     */
    public void deleteTeam(Long teamId) throws EntityNotFoundException, IllegalAccessException {
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("TeamId must be a positive integer");
        }

        Team storedTeam = teamStorage.getTeamById(teamId);
        if (storedTeam == null) {
            throw new EntityNotFoundException("Team with id " + teamId + " not found");
        }
        teamStorage.deleteAllTeamMembersByTeam(teamId);
        teamStorage.deleteTeam(teamId);
    }

    /**
     * Retrieves the list of Teams with offset, limit and a keyword that can be
     * empty
     *
     * @return List of {@link Team} that contains the list of Teams
     */
    public List<TeamMember> getTeamsList(String userName) {
        return teamStorage.getTeamsByUser(userName);
    }


    /**
     * Retrieves the list of Teams with offset, limit and a keyword that can be
     * empty
     *
     * @return List of {@link Team} that contains the list of Teams
     */
    public List<TeamMember> getMembersList(long teamID) {
        return teamStorage.getMembersByTeam(teamID);
    }


    /**
     * Retrieves the list of Teams with offset, limit and a keyword that can be
     * empty
     *
     * @return List of {@link Team} that contains the list of Teams
     */
    public List<Team> getTeams() {
        return teamStorage.getTeams();
    }


    /**
     * Create new TeamMember that will be available for all users. If the TeamMember
     * already exits an {@link EntityExistsException} will be thrown.
     *
     * @param teamMember TeamMember to create
     * @return stored {@link TeamMember} in datasource
     * @throws Exception when TeamMember already exists or an error occurs while
     *                   creating TeamMember or its attached image
     */
    public TeamMember createTeamMember(TeamMember teamMember) throws Exception {
        if (teamMember == null) {
            throw new IllegalArgumentException("TeamMember is mandatory");
        }
        if (teamStorage.getMemberByTeamUserAndRole(teamMember.getTeam().getId(),teamMember.getUserName(),teamMember.getRole()) != null) {
            throw new EntityExistsException("TeamMember Already exist");
        }
        return teamStorage.createTeamMember(teamMember);

    }


    /**
     * Update an existing Project on datasource. If the Project doesn't exit an
     * {@link EntityNotFoundException} will be thrown.
     *
     * @param teamMember dto to update on store
     * @return stored {@link TeamMember} in datasource
     * @throws Exception when {@link EntityNotFoundException} is thrown or an error
     *           occurs while saving Project
     */
    public TeamMember updateTeamMember(TeamMember teamMember) throws Exception {
        if (teamMember == null) {
            throw new IllegalArgumentException("TeamMember is mandatory");
        }
        Long teamMemberId = teamMember.getId();
        if (teamMemberId == null) {
            throw new EntityNotFoundException("TeamMember with null id wasn't found");
        }
        TeamMember storedProject = teamStorage.getTeamMemberById(teamMemberId);
        if (storedProject == null) {
            throw new EntityNotFoundException("TeamMember with id " + teamMemberId + " wasn't found");
        }
        return teamStorage.updateTeamMember(teamMember);
    }



    /**
     * Delete TeamMember identified by its id and check if username has permission to
     * delete it.
     *
     * @param teamMemberId technical identifier of TeamMember
     * @throws EntityNotFoundException if TeamMember wasn't found
     * @throws IllegalAccessException  if user is not allowed to delete TeamMember
     */
    public void deleteTeamMember(Long teamMemberId) throws EntityNotFoundException, IllegalAccessException {
        if (teamMemberId == null || teamMemberId <= 0) {
            throw new IllegalArgumentException("TeamMemberId must be a positive integer");
        }

        TeamMember storedTeamMember = teamStorage.getTeamMemberById(teamMemberId);
        if (storedTeamMember == null) {
            throw new EntityNotFoundException("TeamMember with id " + teamMemberId + " not found");
        }
        teamStorage.deleteTeamMember(teamMemberId);
    }

}
