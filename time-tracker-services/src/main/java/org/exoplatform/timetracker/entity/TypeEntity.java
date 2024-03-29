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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * <p>TypeEntity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "TypeEntity")
@ExoEntity
@Table(name = "ADDONS_TT_TYPE")
@Data
@NamedQueries({})
public class TypeEntity {

  @Id
  @SequenceGenerator(name = "SEQ_TYPE_ID", sequenceName = "SEQ_TYPE_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TYPE_ID")
  @Column(name = "ID")
  private Long   id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "LABEL")
  private String label;


  /**
   * <p>Constructor for TypeEntity.</p>
   */
  public TypeEntity() {
  }

  /**
   * <p>Constructor for TypeEntity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param code a {@link java.lang.String} object.
   * @param label a {@link java.lang.String} object.
   */
  public TypeEntity(Long id, String code, String label) {
    this.id = id;
    this.code = code;
    this.label = label;

  }

}
