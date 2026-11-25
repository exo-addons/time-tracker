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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A timesheet entry as exposed to the AI agent.
 *
 * @param id entry id
 * @param date day of the entry, yyyy-MM-dd
 * @param hours time spent, in hours
 * @param description what was done
 * @param activityId activity id
 * @param activityCode activity code
 * @param activityLabel activity label
 * @param clientId client id
 * @param client client label
 * @param projectId project id
 * @param project project label
 * @param salesOrderId sales order id
 * @param salesOrder sales order name
 * @param projectVersion project version
 * @param location work location
 * @param office office
 * @param username owner of the entry
 */
@JsonInclude(Include.NON_EMPTY)
public record TimeEntryModel(@JsonProperty("entry_id") Long id,
                             String date,
                             float hours,
                             String description,
                             @JsonProperty("activity_id") Long activityId,
                             @JsonProperty("activity_code") String activityCode,
                             @JsonProperty("activity_label") String activityLabel,
                             @JsonProperty("client_id") Long clientId,
                             String client,
                             @JsonProperty("project_id") Long projectId,
                             String project,
                             @JsonProperty("sales_order_id") Long salesOrderId,
                             @JsonProperty("sales_order") String salesOrder,
                             @JsonProperty("project_version") String projectVersion,
                             String location,
                             String office,
                             String username) {
}
