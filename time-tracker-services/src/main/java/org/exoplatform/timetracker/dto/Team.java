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

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Team class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */

@Data
public class Team implements Serializable {


  private String              id;

  private String            Name;

  private String            description;


  /**
   * <p>Constructor for Team.</p>
   */
  public Team() {
  }

  /**
   * <p>Constructor for Team.</p>
   *
   * @param id a {@link java.lang.String} object.
   * @param Name a {@link java.lang.String} object.
   * @param description a {@link java.lang.String} object.
   */
  public Team(String id, String Name, String description) {
    this.id = id;
    this.Name = Name;
    this.description = description;

  }

}
