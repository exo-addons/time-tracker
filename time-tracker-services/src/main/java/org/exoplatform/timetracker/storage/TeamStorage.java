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
package org.exoplatform.timetracker.storage;

import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.timetracker.dao.TeamDAO;
import org.exoplatform.timetracker.dao.TeamMemberDAO;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.entity.TeamEntity;
import org.exoplatform.timetracker.entity.TeamMemberEntity;
import org.gatein.api.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Storage service to access / load and save Teams. This service will be
 * used , as well, to convert from JPA entity to DTO.
 */
public class TeamStorage {

  private final TeamDAO teamDAO;
  private final TeamMemberDAO teamMemberDAO;
  private final IdentityManager identityManager;

  public TeamStorage(TeamDAO teamDAO, TeamMemberDAO teamMemberDAO, IdentityManager identityManager) {
    this.teamDAO = teamDAO;
    this.teamMemberDAO = teamMemberDAO;
    this.identityManager = identityManager;
  }


  /////////////////////////Team storage /////////////////////////////////////////

  public Team createTeam(Team team) throws Exception {
    if (team == null) {
      throw new IllegalArgumentException("Team is mandatory");
    }
    TeamEntity teamEntity = toEntity(team);
    team.setId(null);
    teamEntity = teamDAO.create(teamEntity);
    return toDTO(teamEntity);
  }

  public Team updateTeam(Team team) throws Exception {
    if (team == null) {
      throw new IllegalArgumentException("Team is mandatory");
    }
    Long teamId = team.getId();
    TeamEntity teamEntity = teamDAO.find(team.getId());
    if (teamEntity == null) {
      throw new EntityNotFoundException("Team with id " + teamId + " wasn't found");
    }

    teamEntity = toEntity(team);
    teamEntity = teamDAO.update(teamEntity);

    return toDTO(teamEntity);
  }

  public void deleteTeam(long teamId) throws EntityNotFoundException {
    if (teamId <= 0) {
      throw new IllegalArgumentException("TeamId must be a positive integer");
    }
    TeamEntity teamEntity = teamDAO.find(teamId);
    if (teamEntity == null) {
      throw new EntityNotFoundException("Team with id " + teamId + " not found");
    }
    teamDAO.delete(teamEntity);
  }

  public Team getTeamById(long TeamId) {
    if (TeamId <= 0) {
      throw new IllegalArgumentException("TeamId must be a positive integer");
    }
    TeamEntity TeamEntity = teamDAO.find(TeamId);
    return toDTO(TeamEntity);
  }

  public List<Team> getTeams() {
    List<TeamEntity> teams = teamDAO.findAll();
    return teams.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public List<TeamMember> getTeamsByUser(String userName) {
    List<TeamMemberEntity> applicatiions = teamMemberDAO.getTeamsByUser(userName);
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }


  public List<TeamMember> getMembersByTeam(long teamId) {
    List<TeamMemberEntity> applicatiions = teamMemberDAO.getMembersByTeam(teamId);
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }


  public Team toDTO(TeamEntity teamEntity) {
    if (teamEntity == null) {
      return null;
    }
    return new Team(teamEntity.getId(),
            teamEntity.getName(),
            teamEntity.getDescription());
  }

  public TeamEntity toEntity(Team team) {
    if (team == null) {
      return null;
    }
    return new TeamEntity(team.getId(),
            team.getName(),
            team.getDescription());
  }

  public List<Team> toDtos(List<TeamEntity> teams){
    return teams.stream().map(this::toDTO).collect(Collectors.toList());
  }

/////////////////////////Team Fields storage /////////////////////////////////////////

  public TeamMember createTeamMember(TeamMember feamMember) throws Exception {
    if (feamMember == null) {
      throw new IllegalArgumentException("TeamMember is mandatory");
    }
    TeamMemberEntity feamMemberEntity = toEntity(feamMember);
    feamMember.setId(null);
    feamMemberEntity = teamMemberDAO.create(feamMemberEntity);
    return toDTO(feamMemberEntity);
  }

  public TeamMember updateTeamMember(TeamMember feamMember) throws Exception {
    if (feamMember == null) {
      throw new IllegalArgumentException("TeamMember is mandatory");
    }
    Long feamMemberId = feamMember.getId();
    TeamMemberEntity feamMemberEntity = teamMemberDAO.find(feamMember.getId());
    if (feamMemberEntity == null) {
      throw new EntityNotFoundException("TeamMember with id " + feamMemberId + " wasn't found");
    }

    feamMemberEntity = toEntity(feamMember);
    feamMemberEntity = teamMemberDAO.update(feamMemberEntity);

    return toDTO(feamMemberEntity);
  }

  public void deleteTeamMember(long feamMemberId) throws EntityNotFoundException {
    if (feamMemberId <= 0) {
      throw new IllegalArgumentException("TeamMemberId must be a positive integer");
    }
    TeamMemberEntity feamMemberEntity = teamMemberDAO.find(feamMemberId);
    if (feamMemberEntity == null) {
      throw new EntityNotFoundException("TeamMember with id " + feamMemberId + " not found");
    }
    teamMemberDAO.delete(feamMemberEntity);
  }

  public void deleteAllTeamMembersByTeam(long teamId) throws EntityNotFoundException {
    if (teamId <= 0) {
      throw new IllegalArgumentException("TeamMemberId must be a positive integer");
    }
    List<TeamMemberEntity> teamMemberEntities = teamMemberDAO.getMembersByTeam(teamId);
    teamMemberDAO.deleteAll(teamMemberEntities);
  }


  public TeamMember getTeamMemberById(long teamMemberId) {
    if (teamMemberId <= 0) {
      throw new IllegalArgumentException("TeamMemberId must be a positive integer");
    }
    TeamMemberEntity TeamMemberEntity = teamMemberDAO.find(teamMemberId);
    return toDTO(TeamMemberEntity);
  }


  public TeamMember  getMemberByTeamUserAndRole(long teamId, String userName, String role) {
    TeamMemberEntity TeamMemberEntity = teamMemberDAO.getMemberByTeamUserAndRole(teamId, userName, role);
    return toDTO(TeamMemberEntity);
  }


  public TeamMember toDTO(TeamMemberEntity teamMemberEntity) {
    if (teamMemberEntity == null) {
      return null;
    }
    TeamMember teamMember = new TeamMember(teamMemberEntity.getId(),
            teamMemberEntity.getUserName(),
            teamMemberEntity.getRole(),
            toDTO(teamMemberEntity.getTeamEntity()));
    Identity identity =identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME,teamMember.getUserName());
    if(identity != null) {
      teamMember.setFullName(identity.getProfile().getFullName());
    }
    return teamMember;
  }

  public TeamMemberEntity toEntity(TeamMember teamMember) {
    if (teamMember == null) {
      return null;
    }
    return new TeamMemberEntity(teamMember.getId(),
            teamMember.getUserName(),
            teamMember.getRole(),
            toEntity(teamMember.getTeam()));
  }


}
