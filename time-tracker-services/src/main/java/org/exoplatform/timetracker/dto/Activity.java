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

import org.exoplatform.timetracker.entity.FeatureEntity;
import org.exoplatform.timetracker.entity.ProjectEntity;

import lombok.Data;

/**
 * @author Krout MedAmine
 */

@Data
public class Activity implements Serializable {

  private static final long serialVersionUID = -9063665272752521543L;

  private Long              id;

  private String            code;

  private Type            type;

  private SubType            subType;

  private ActivityCode            activityCode;

  private SubActivityCode            activityCode;

  private String            label;

  private Project    project;

  private Feature    feature;

  public Activity() {
  }

  public Activity(Long id,
                  Type type,
                  SubType subType,
                  ActivityCode activityCode,
                  SubActivityCode subActivityCode,
                  String label,
                  Project project,
                  Feature feature) {
    this.id = id;
    this.type = type;
    this.subType = subType;
    this.activityCode = activityCode;
    this.subActivityCode = subActivityCode;
    this.label = label;
    this.project = project;
    this.feature = feature;
  }

}
