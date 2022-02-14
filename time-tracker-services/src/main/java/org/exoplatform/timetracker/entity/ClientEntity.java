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

import javax.persistence.*;

import org.exoplatform.commons.api.persistence.ExoEntity;

import lombok.Data;

/**
 * <p>ClientEntity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "ClientEntity")
@ExoEntity
@Table(name = "ADDONS_TT_CLIENT")
@Data
@NamedQueries({})
public class ClientEntity {

  @Id
  @SequenceGenerator(name = "SEQ_CLIENT_ID", sequenceName = "SEQ_CLIENT_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CLIENT_ID")
  @Column(name = "ID")
  private Long   id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "LABEL")
  private String label;

  /**
   * <p>Constructor for ClientEntity.</p>
   */
  public ClientEntity() {
  }

  /**
   * <p>Constructor for ClientEntity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param code a {@link java.lang.String} object.
   * @param label a {@link java.lang.String} object.
   */
  public ClientEntity(Long id, String code, String label) {
    this.id = id;
    this.code = code;
    this.label = label;
  }

}
