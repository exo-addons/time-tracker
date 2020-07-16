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
 * @author Krout MedAmine
 */

@Data
public class ActivityRecord implements Serializable {

  private static final long serialVersionUID = -9214989944414271753L;

  private Long              id;

  private String            userName;

  private String            activityDate;

  private Date              from;

  private Date              to;

  private String            description;

  private String            location;

  private String            office;

  private int               time;

  private SalesOrder        salesOrder;

  private String            projectVersion;

  private Client            client;

  private Activity          activity;

  private Date         createdDate;

  public ActivityRecord() {
  }

  public ActivityRecord(Long id,
                        String userName,
                        String activityDate,
                        Date from,
                        Date to,
                        String description,
                        String location,
                        String office,
                        int time,
                        String projectVersion,
                        Client client,
                        Activity activity,
                        SalesOrder salesOrder,
                        Date         createdDate) {
    this.id = id;
    this.userName = userName;
    this.activityDate = activityDate;
    this.from = from;
    this.to = to;
    this.description = description;
    this.location = location;
    this.office = office;
    this.time = time;
    this.salesOrder = salesOrder;
    this.projectVersion = projectVersion;
    this.client = client;
    this.activity = activity;
    this.createdDate = createdDate;
  }

}
