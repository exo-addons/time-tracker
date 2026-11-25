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
 * A client, project, sales order, location, office or team, as exposed to the
 * AI agent.
 *
 * @param id technical id, when the item has one
 * @param code code of the item
 * @param label label (name) of the item
 * @param clientId id of the client the item belongs to, when any
 */
@JsonInclude(Include.NON_EMPTY)
public record TimeReferenceModel(Long id,
                                 String code,
                                 String label,
                                 @JsonProperty("client_id") Long clientId) {
}
