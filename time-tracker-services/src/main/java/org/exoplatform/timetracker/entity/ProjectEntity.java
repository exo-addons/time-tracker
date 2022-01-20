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
 * <p>ProjectEntity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "ProjectEntity")
@ExoEntity
@Table(name = "ADDONS_TT_PROJECT")
@Data
@NamedQueries({})
public class ProjectEntity {

  @Id
  @SequenceGenerator(name = "SEQ_PROJECT_ID", sequenceName = "SEQ_PROJECT_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PROJECT_ID")
  @Column(name = "ID")
  private Long         id;

  @Column(name = "CODE")
  private String       code;

  @Column(name = "LABEL")
  private String       label;

  @ManyToOne
  @JoinColumn(name = "CLIENT_ID")
  private ClientEntity clientEntity;

  /**
   * <p>Constructor for ProjectEntity.</p>
   */
  public ProjectEntity() {
  }

  /**
   * <p>Constructor for ProjectEntity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param code a {@link java.lang.String} object.
   * @param label a {@link java.lang.String} object.
   * @param clientEntity a {@link org.exoplatform.timetracker.entity.ClientEntity} object.
   */
  public ProjectEntity(Long id, String code, String label, ClientEntity clientEntity) {
    this.id = id;
    this.code = code;
    this.label = label;
    this.clientEntity = clientEntity;
  }

}
