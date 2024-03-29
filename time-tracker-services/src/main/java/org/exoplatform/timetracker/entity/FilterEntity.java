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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * <p>FilterEntity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "FilterEntity")
@ExoEntity
@Table(name = "ADDONS_TT_FILTER")
@Data
@NamedQueries({
        @NamedQuery(name = "FilterEntity.getFiltersByUserName", query = "SELECT filter FROM FilterEntity filter where filter.userName = :userName ") })
public class FilterEntity {

  @Id
  @SequenceGenerator(name = "SEQ_FILTER_ID", sequenceName = "SEQ_FILTER_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FILTER_ID")
  @Column(name = "ID")
  private Long   id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "USER_NAME")
  private String userName;


  /**
   * <p>Constructor for FilterEntity.</p>
   */
  public FilterEntity() {
  }

  /**
   * <p>Constructor for FilterEntity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param name a {@link java.lang.String} object.
   * @param userName a {@link java.lang.String} object.
   */
  public FilterEntity(Long id, String name, String userName) {
    this.id = id;
    this.name = name;
    this.userName = userName;

  }

}
