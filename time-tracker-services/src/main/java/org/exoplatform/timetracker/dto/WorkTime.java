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
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * Type class.
 * </p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */

@Data
public class WorkTime implements Serializable {

  private Long    id;

  private Date    from;

  private Date    to;

  private String    fromDate;

  private String    toDate;

  private Float   time;

  private String  period;

  private String  team;

  private String  userId;

  private boolean defaultTime;

  private Office  office;

  /**
   * <p>
   * Constructor for WorkTime.
   * </p>
   */
  public WorkTime() {
  }

  /**
   * <p>
   * Constructor for WorkTime.
   * </p>
   * @param id a {@link String} WorkTime object Id.
   * @param from a {@link Date} WorkTime from date.
   * @param to a {@link Date} WorkTime from date.
   * @param fromDate a {@link Date} WorkTime from date .
   * @param toDate a {@link Date} WorkTime from date.
   * @param time a {@link float} number of hours.
   * @param period a {@link String} period type can be week, day or mounth.
   * @param team a {@link String} related team.
   * @param userId a {@link String} rlated user id.
   * @param office a {@link Office} related office.
   * @param defaultTime a {@link Boolean} true it this plan is the default to use.
   */
  public WorkTime(Long id,
                  Date from,
                  Date to,
                  String fromDate,
                  String toDate,
                  float time,
                  String period,
                  String team,
                  String userId,
                  Office office,
                  boolean defaultTime) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.time = time;
    this.office = office;
    this.team = team;
    this.userId = userId;
    this.period = period;
    this.defaultTime = defaultTime;
  }

}
