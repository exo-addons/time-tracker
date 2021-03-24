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

import lombok.Data;

/**
 * <p>Feature class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */

@Data
public class Feature implements Serializable {

  private static final long serialVersionUID = -201134276256482487L;

  private Long              id;

  private String            code;

  private String            label;

  private String            spec;

  private String            exo;

  private String            displayLabel;

  /**
   * <p>Constructor for Feature.</p>
   */
  public Feature() {
  }

  /**
   * <p>Constructor for Feature.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param code a {@link java.lang.String} object.
   * @param label a {@link java.lang.String} object.
   * @param spec a {@link java.lang.String} object.
   * @param exo a {@link java.lang.String} object.
   */
  public Feature(Long id, String code, String label, String spec, String exo) {
    this.id = id;
    this.code = code;
    this.label = label;
    this.spec = spec;
    this.exo = exo;
    this.displayLabel = code +" - "+label;
  }

}
