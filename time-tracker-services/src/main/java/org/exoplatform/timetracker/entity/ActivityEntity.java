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
 * <p>ActivityEntity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "ActivityEntity")
@ExoEntity
@Table(name = "ADDONS_TT_ACTIVITY")
@Data
@NamedQueries({
	 @NamedQuery(name = "ActivityEntity.getActivities", query = "SELECT activity FROM ActivityEntity activity ORDER BY activity.label")
})
public class ActivityEntity {

  @Id
  @SequenceGenerator(name = "SEQ_PROJECT_ID", sequenceName = "SEQ_PROJECT_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PROJECT_ID")
  @Column(name = "ID")
  private Long          id;

  @Column(name = "ACTIVITY_LABEL")
  private String        label;

  @ManyToOne
  @JoinColumn(name = "PROJECT_ID")
  private ProjectEntity projectEntity;

  @ManyToOne
  @JoinColumn(name = "FEATURE_ID")
  private FeatureEntity featureEntity;

  @ManyToOne
  @JoinColumn(name = "ACTIVITY_CODE_ID")
  private ActivityCodeEntity activityCodeEntity;

  @ManyToOne
  @JoinColumn(name = "SUB_ACTIVITY_CODE_ID")
  private SubActivityCodeEntity subActivityCodeEntity;

  @ManyToOne
  @JoinColumn(name = "TYPE_ID")
  private TypeEntity typeEntity;

  @ManyToOne
  @JoinColumn(name = "SUB_TYPE_ID")
  private SubTypeEntity subTypeEntity;

  /**
   * <p>Constructor for ActivityEntity.</p>
   */
  public ActivityEntity() {
  }

  /**
   * <p>Constructor for ActivityEntity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param label a {@link java.lang.String} object.
   * @param projectEntity a {@link org.exoplatform.timetracker.entity.ProjectEntity} object.
   * @param activityCodeEntity a {@link org.exoplatform.timetracker.entity.ActivityCodeEntity} object.
   * @param subActivityCodeEntity a {@link org.exoplatform.timetracker.entity.SubActivityCodeEntity} object.
   * @param typeEntity a {@link org.exoplatform.timetracker.entity.TypeEntity} object.
   * @param subTypeEntity a {@link org.exoplatform.timetracker.entity.SubTypeEntity} object.
   * @param featureEntity a {@link org.exoplatform.timetracker.entity.FeatureEntity} object.
   */
  public ActivityEntity(Long id,
                        String label,
                        ProjectEntity projectEntity,
                        ActivityCodeEntity activityCodeEntity,
                        SubActivityCodeEntity subActivityCodeEntity,
                        TypeEntity typeEntity,
                        SubTypeEntity subTypeEntity,
                        FeatureEntity featureEntity) {
    this.id = id;
    this.label = label;
    this.projectEntity = projectEntity;
    this.activityCodeEntity = activityCodeEntity;
    this.subActivityCodeEntity = subActivityCodeEntity;
    this.typeEntity = typeEntity;
    this.subTypeEntity = subTypeEntity;
    this.featureEntity = featureEntity;
  }

}
