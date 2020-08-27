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

import lombok.Data;
import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;

/**
 * @author Krout MedAmine
 */
@Entity(name = "FilterFieldEntity")
@ExoEntity
@Table(name = "ADDONS_TT_FILTER_FIELD")
@Data
@NamedQueries({
        @NamedQuery(name = "FilterFieldEntity.getFieldsByFilter", query = "SELECT field FROM FilterFieldEntity field where field.filterEntity.id = :filerId ") })

public class FilterFieldEntity {

  @Id
  @SequenceGenerator(name = "SEQ_FILTER_ID", sequenceName = "SEQ_FILTER_ID")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FILTER_ID")
  @Column(name = "ID")
  private Long   id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "VALUE")
  private String value;


  @ManyToOne
  @JoinColumn(name = "FILTER_ID")
  private FilterEntity   filterEntity;


  public FilterFieldEntity() {
  }

  public FilterFieldEntity(Long id, String name, String value,FilterEntity filterEntity) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.filterEntity = filterEntity;

  }

}
