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
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Totals of a timesheet over a date range.
 *
 * @param username owner of the timesheet
 * @param fromDate first day of the range
 * @param toDate last day of the range
 * @param totalHours total hours over the range
 * @param entryCount number of entries over the range
 * @param hoursByDay hours per day with at least one entry
 * @param hoursByActivity hours per activity label
 * @param hoursByProject hours per project label
 * @param hoursByClient hours per client label
 * @param emptyWeekdays Monday-to-Friday days without any entry
 * @param underExpectedWeekdays Monday-to-Friday days below the expected hours,
 *          only when an expectation was given
 */
@JsonInclude(Include.NON_NULL)
public record TimesheetSummaryModel(String username,
                                    @JsonProperty("from_date") String fromDate,
                                    @JsonProperty("to_date") String toDate,
                                    @JsonProperty("total_hours") float totalHours,
                                    @JsonProperty("entry_count") int entryCount,
                                    @JsonProperty("hours_by_day") Map<String, Float> hoursByDay,
                                    @JsonProperty("hours_by_activity") Map<String, Float> hoursByActivity,
                                    @JsonProperty("hours_by_project") Map<String, Float> hoursByProject,
                                    @JsonProperty("hours_by_client") Map<String, Float> hoursByClient,
                                    @JsonProperty("empty_weekdays") List<String> emptyWeekdays,
                                    @JsonProperty("under_expected_weekdays") List<String> underExpectedWeekdays) {
}
