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
package org.exoplatform.timetracker.entity;

import lombok.Data;
import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;

/**
 * @author Krout MedAmine
 */
@Entity(name = "TeamMemberEntity")
@ExoEntity
@Table(name = "ADDONS_TT_TEAM_MEMBER")
@Data
@NamedQueries({
        @NamedQuery(name = "TeamMemberEntity.getTeamsByUser", query = "SELECT team FROM TeamMemberEntity team where team.userName = :userName"),
        @NamedQuery(name = "TeamMemberEntity.getMembersByTeam", query = "SELECT team FROM TeamMemberEntity team where team.teamEntity.id = :teamId"),
        @NamedQuery(name = "TeamMemberEntity.getMemberByTeamUserAndRole", query = "SELECT team FROM TeamMemberEntity team where team.teamEntity.id = :teamId and team.userName = :userName and team.role = :role"),
        })

public class TeamMemberEntity {

  @Id
  @SequenceGenerator(name = "SEQ_TEAM_MEMBER_ID", sequenceName = "SEQ_TEAM_MEMBER_ID")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TEAM_MEMBER_ID")
  @Column(name = "ID")
  private Long   id;


  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "ROLE")
  private String role;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
  private TeamEntity         teamEntity;

  public TeamMemberEntity() {
  }

  public TeamMemberEntity(Long id, String userName, String role, TeamEntity teamEntity) {
    this.id = id;
    this.userName = userName;
    this.role = role;
    this.teamEntity = teamEntity;
  }

}
