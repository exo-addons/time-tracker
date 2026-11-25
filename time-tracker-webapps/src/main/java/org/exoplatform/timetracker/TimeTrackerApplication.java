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
package org.exoplatform.timetracker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.elasticsearch.autoconfigure.ElasticsearchClientAutoConfiguration;
import org.springframework.boot.elasticsearch.autoconfigure.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

import io.meeds.spring.AvailableIntegration;
import io.meeds.spring.kernel.PortalApplicationContextInitializer;

/**
 * Spring context of the Time Tracker add-on. The services of this add-on are
 * Kernel components, so the only Spring beans are the MCP tools
 * ({@code org.exoplatform.timetracker.mcp}, active under the
 * {@code mcp-server} profile). Without this context, the MCP Server never
 * discovers them: Spring beans are shared with the other contexts through the
 * Kernel, one context per webapp.
 */
@SpringBootApplication(scanBasePackages = {
  TimeTrackerApplication.MODULE_NAME,
  AvailableIntegration.KERNEL_MODULE,
}, exclude = {
  LiquibaseAutoConfiguration.class,
  DataSourceAutoConfiguration.class,
  DataSourceTransactionManagerAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class,
  SecurityAutoConfiguration.class,
  ElasticsearchClientAutoConfiguration.class,
  ElasticsearchRestClientAutoConfiguration.class,
})
@PropertySource("classpath:application.properties")
public class TimeTrackerApplication extends PortalApplicationContextInitializer {

  public static final String MODULE_NAME = "org.exoplatform.timetracker.mcp";

}
