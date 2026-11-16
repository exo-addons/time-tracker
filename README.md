# exo-time-tracker
## MCP tools (AI agent)

When the MCP Server add-on is deployed (Spring profile `mcp-server`), the module exposes its timesheet to AI agents
through `TimeTrackerMcpTool`. The tool definitions (names, schemas, approval flags) are in
`time-tracker-services/src/main/resources/ai-tool-definitions.json`; a tool method without an entry there is silently
dropped by the server, and `TimeTrackerMcpToolTest` checks both sides match.

Every tool acts as the signed-in user.

| Tool | What it does | Approval |
| --- | --- | --- |
| `get_time_tracker_activities` | Activities the user can book on (their teams') | |
| `list_time_tracker_clients` / `_projects` / `_sales_orders` | Clients, projects and sales orders an entry can carry | |
| `get_time_tracker_locations` | Locations and offices | |
| `list_time_entries` | The user's entries, newest first | |
| `get_last_time_entry` | The user's most recent entry | |
| `get_timesheet_summary` | Hours per day / activity / project / client, empty weekdays, weekdays below an expected number of hours | |
| `create_time_entry` | Book time on an activity | yes |
| `update_time_entry` | Change only the given fields of an entry | yes |
| `duplicate_time_entry` | Copy an entry over a date range (weekends skipped by default) | yes |
| `delete_time_entry` | Delete an entry (owner, or a time-tracking manager as in the UI) | yes |
| `list_time_tracker_teams` | All teams (managers only) | |

Writes only concern the user's own timesheet. Time-tracking managers (`/platform/time-tracking-managers`) can also pass
`username` or `team_id` to `list_time_entries`, and `username` to `get_timesheet_summary`, to read other people's time.
Exports and the administration of clients, projects, activities and teams are not exposed.
