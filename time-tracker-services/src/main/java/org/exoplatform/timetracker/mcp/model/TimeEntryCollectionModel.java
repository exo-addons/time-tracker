/**
 * Copyright (C) 2026 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.timetracker.mcp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A page of timesheet entries as exposed to the AI agent.
 *
 * @param entries the entries of this page
 * @param total number of entries matching, all pages together
 * @param offset offset of this page
 * @param limit page size
 * @param totalHours sum of the hours of this page
 */
public record TimeEntryCollectionModel(List<TimeEntryModel> entries,
                                       long total,
                                       int offset,
                                       int limit,
                                       @JsonProperty("total_hours") float totalHours) {
}
