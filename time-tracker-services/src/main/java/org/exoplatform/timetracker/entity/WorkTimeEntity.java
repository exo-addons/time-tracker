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

import org.exoplatform.commons.api.persistence.ExoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * <p>
 * WorkTimeEntity class.
 * </p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
@Entity(name = "WorkTimeEntity")
@ExoEntity
@Table(name = "ADDONS_TT_WORK_TIME")
@Data
@NamedQueries({})
public class WorkTimeEntity {

  @Id
  @SequenceGenerator(name = "SEQ_WORK_TIME_ID", sequenceName = "SEQ_WORK_TIME_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_WORK_TIME_ID")
  @Column(name = "ID")
  private Long         id;

  @Column(name = "FROM")
  private Date         from;

  @Column(name = "TO")
  private Date         to;

  @Column(name = "TIME")
  private Float        time;

  @Column(name = "PERIOD")
  private String       period;

  @Column(name = "TEAM")
  private String       team;

  @Column(name = "USER_ID")
  private String       userId;

  @Column(name = "DEFAULT_TIME")
  private boolean      defaultTime;

  @ManyToOne
  @JoinColumn(name = "OFFICE_CODE")
  private OfficeEntity officeEntity;

  /**
   * <p>
   * Constructor for WorkTimeEntity.
   * </p>
   */
  public WorkTimeEntity() {
  }

  /**
   * <p>
   * Constructor for WorkTimeEntity.
   * </p>
   * @param id a {@link String} WorkTime object Id.
   * @param from a {@link Date} WorkTime from date.
   * @param to a {@link Date} WorkTime from date.
   * @param time a {@link float} number of hours.
   * @param period a {@link String} period type can be week, day or mounth.
   * @param team a {@link String} related team.
   * @param userId a {@link String} rlated user id.
   * @param officeEntity a {@link OfficeEntity} related office.
   * @param defaultTime a {@link Boolean} true it this plan is the default to use.
   */
  public WorkTimeEntity(Long id,
                        Date from,
                        Date to,
                        float time,
                        String period,
                        String team,
                        String userId,
                        OfficeEntity officeEntity,
                        boolean defaultTime) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.time = time;
    this.officeEntity = officeEntity;
    this.team = team;
    this.userId = userId;
    this.period = period;
    this.defaultTime = defaultTime;
  }

}
