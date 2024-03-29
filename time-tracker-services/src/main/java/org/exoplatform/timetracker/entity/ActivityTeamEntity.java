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

import java.io.Serializable;

import org.exoplatform.commons.api.persistence.ExoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author Krout MedAmine
 */
@Entity(name = "ActivityTeamEntity")
@ExoEntity
@Table(name = "ADDONS_TT_ACTIVITY_TEAM")
@Data
@NamedQueries({
        @NamedQuery(name = "ActivityTeamEntity.getActivitiesByTeams", query = "SELECT activity FROM ActivityTeamEntity activity where activity.teamId in :teams"),
        @NamedQuery(name = "ActivityTeamEntity.getTeamsByActivity", query = "SELECT team FROM ActivityTeamEntity team where team.activityEntity.id = :activityId"),

})
public class ActivityTeamEntity implements Serializable {
  @Id
  @ManyToOne
  @JoinColumn(name = "ACTIVITY_ID")
  private ActivityEntity activityEntity;
  @Id
  @Column(name = "TEAM_ID")
  private String teamId;

  public ActivityTeamEntity() {
  }

  public ActivityTeamEntity(ActivityEntity activityEntity, String teamId) {
    this.activityEntity = activityEntity;
    this.teamId = teamId;
  }

}
