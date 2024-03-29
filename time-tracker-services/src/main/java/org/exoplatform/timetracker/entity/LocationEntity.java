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

import org.exoplatform.commons.api.persistence.ExoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * <p>
 * TypeEntity class.
 * </p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "LocationEntity")
@ExoEntity
@Table(name = "ADDONS_TT_LOCATION")
@Data
@NamedQueries({})
public class LocationEntity {

  @Id
  @Column(name = "CODE")
  private String code;

  @Column(name = "LABEL")
  private String label;

  /**
   * <p>
   * Constructor for LocationEntity.
   * </p>
   */
  public LocationEntity() {
  }

  /**
   * <p>
   * Constructor for TypeEntity.
   * </p>
   *
   * @param code a {@link String} object.
   * @param label a {@link String} object.
   */
  public LocationEntity(String code, String label) {
    this.code = code;
    this.label = label;

  }

}
