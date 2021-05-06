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

import java.util.Date;

import javax.persistence.*;

import org.exoplatform.commons.api.persistence.ExoEntity;

import lombok.Data;

/**
 * <p>ActivityRecordEntity class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "ActivityRecordEntity")
@ExoEntity
@Table(name = "ADDONS_TT_ACTIVITY_RECORD")
@Data
@NamedQueries({
        @NamedQuery(name = "ActivityRecordEntity.getActivityRecordsByDay", query = "SELECT activity FROM ActivityRecordEntity activity where activity.activityDate = :day and activity.userName = :userName "),
        @NamedQuery(name = "ActivityRecordEntity.getLastActivityRecord", query = "SELECT activity FROM ActivityRecordEntity activity where  activity.userName = :userName ORDER BY  activity.id DESC") })
public class ActivityRecordEntity {


  @Id
  @SequenceGenerator(name = "SEQ_PROJECT_ID", sequenceName = "SEQ_PROJECT_ID")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PROJECT_ID")
  @Column(name = "ID")
  private Long           id;
  @Column(name = "USER_NAME")
  private String         userName;
  @Column(name = "ACTIVITY_DATE")
  private String         activityDate;
  @Column(name = "ACTIVITY_TIME")
  private Date           activityTime;

  @Column(name = "DESCRIPTION")
  private String         description;

  @Column(name = "LOCATION")
  private String         location;

  @Column(name = "OFFICE")
  private String         office;

  @Column(name = "TIME")
  private Float            time;

  @Column(name = "PROJECT_VERSION")
  private String         projectVersion;

  @ManyToOne
  @JoinColumn(name = "CLIENT_ID")
  private ClientEntity   clientEntity;

  @ManyToOne
  @JoinColumn(name = "ACTIVITY_ID")
  private ActivityEntity activityEntity;

  @ManyToOne
  @JoinColumn(name = "SALES_ORDER_ID")
  private SalesOrderEntity         salesOrderEntity;

  @Column(name = "CREATED_DATE")
  protected Date         createdDate;




  /**
   * <p>Constructor for ActivityRecordEntity.</p>
   */
  public ActivityRecordEntity() {
  }

  /**
   * <p>Constructor for ActivityRecordEntity.</p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param userName a {@link java.lang.String} object.
   * @param activityDate a {@link java.lang.String} object.
   * @param activityTime a {@link java.util.Date} object.
   * @param description a {@link java.lang.String} object.
   * @param location a {@link java.lang.String} object.
   * @param office a {@link java.lang.String} object.
   * @param time a int.
   * @param projectVersion a {@link java.lang.String} object.
   * @param clientEntity a {@link org.exoplatform.timetracker.entity.ClientEntity} object.
   * @param activityEntity a {@link org.exoplatform.timetracker.entity.ActivityEntity} object.
   * @param salesOrderEntity a {@link org.exoplatform.timetracker.entity.SalesOrderEntity} object.
   * @param createdDate a {@link java.util.Date} object.
   */
  public ActivityRecordEntity(Long id,
                              String userName,
                              String activityDate,
                              Date activityTime,
                              String description,
                              String location,
                              String office,
                              float time,
                              String projectVersion,
                              ClientEntity clientEntity,
                              ActivityEntity activityEntity,
                              SalesOrderEntity salesOrderEntity,
                              Date createdDate) {
    this.id = id;
    this.userName = userName;
    this.activityDate = activityDate;
    this.activityTime = activityTime;
    this.description = description;
    this.location = location;
    this.office = office;
    this.time = time;
    this.salesOrderEntity = salesOrderEntity;
    this.projectVersion = projectVersion;
    this.clientEntity = clientEntity;
    this.activityEntity = activityEntity;
    this.createdDate = createdDate;
  }

}
