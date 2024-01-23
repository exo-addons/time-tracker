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
package org.exoplatform.timetracker.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * <p>Activity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */

@Data
public class Activity implements Serializable {

  private static final long serialVersionUID = -9063665272752521543L;

  private Long              id;

  private String            code;

  private Type            type;

  private SubType            subType;

  private ActivityCode            activityCode;

  private SubActivityCode            subActivityCode;

  private String            label;

  private Project    project;

  private Feature    feature;

  private List<Team> teams;

  /**
   * <p>Constructor for Activity.</p>
   */
  public Activity() {
  }

  /**
   * <p>Constructor for Activity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param type a {@link org.exoplatform.timetracker.dto.Type} object.
   * @param subType a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @param activityCode a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @param subActivityCode a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @param label a {@link java.lang.String} object.
   * @param project a {@link org.exoplatform.timetracker.dto.Project} object.
   * @param feature a {@link org.exoplatform.timetracker.dto.Feature} object.
   * @param teams a {@link java.util.List} object.
   */
  public Activity(Long id,
                  Type type,
                  SubType subType,
                  ActivityCode activityCode,
                  SubActivityCode subActivityCode,
                  String label,
                  Project project,
                  Feature feature,
                  List<Team> teams) {
    this.id = id;
    this.type = type;
    this.subType = subType;
    this.activityCode = activityCode;
    this.subActivityCode = subActivityCode;
    this.label = label;
    this.project = project;
    this.feature = feature;
    this.teams = teams;
  }

}
