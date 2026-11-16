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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Result of duplicating a timesheet entry over a date range.
 *
 * @param source the entry that was duplicated
 * @param created number of entries created
 * @param fromDate first day of the range
 * @param toDate last day of the range
 */
public record TimeEntryDuplicationModel(TimeEntryModel source,
                                        int created,
                                        @JsonProperty("from_date") String fromDate,
                                        @JsonProperty("to_date") String toDate) {
}
