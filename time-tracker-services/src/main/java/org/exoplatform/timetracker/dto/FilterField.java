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

import lombok.Data;

import java.io.Serializable;

/**
 * <p>FilterField class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */

@Data
public class FilterField implements Serializable {


    private Long id;

    private String name;

    private String value;

    private Filter filter;


    /**
     * <p>Constructor for FilterField.</p>
     */
    public FilterField() {
    }

    /**
     * <p>Constructor for FilterField.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @param filter a {@link org.exoplatform.timetracker.dto.Filter} object.
     */
    public FilterField(Long id, String name, String value, Filter filter) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.filter = filter;

    }

}
