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
import java.util.List;

import lombok.Data;

/**
 * <p>
 * Client class.
 * </p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */

@Data
public class Client implements Serializable {

  private static final long serialVersionUID = -4686045812635563243L;

  private Long              id;

  private Long              clientId;

  private String            code;

  private String            label;

  private String            displayLabel;

  private List<SalesOrder> salesOrders;

  /**
   * <p>
   * Constructor for Client.
   * </p>
   */
  public Client() {
  }

  /**
   * <p>
   * Constructor for Client.
   * </p>
   *
   * @param id a {@link java.lang.Long} object.
   * @param code a {@link java.lang.String} object.
   * @param label a {@link java.lang.String} object.
   */
  public Client(Long id, String code, String label) {
    this.id = id;
    this.code = code;
    this.label = label;
    this.displayLabel = code + " - " + label;
  }

  public Client(Long id, String code, String label, List<SalesOrder> salesOrders) {
    this.id = id;
    this.code = code;
    this.label = label;
    this.displayLabel = code + " - " + label;
    this.salesOrders = salesOrders;
  }

}
