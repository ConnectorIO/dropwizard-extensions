/**
 * Copyright (C) 2020 Connectorio Sp. z o.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.connectorio.dropwizard.autobundle.datasource;

import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.PooledDataSourceFactory;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceAutoBundle<T extends Configuration> implements AutomaticBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(DataSourceAutoBundle.class);

  private PooledDataSourceFactory dataSourceFactory;

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    if (configuration instanceof DataSourceAwareConfiguration) {
      this.dataSourceFactory = ((DataSourceAwareConfiguration) configuration).getDataSourceFactory();
    } else {
      logger.warn("Configuration does not ship database connection information. Make sure your configuration implements {}",
        DataSourceAwareConfiguration.class);
    }
  }

  public Optional<PooledDataSourceFactory> getDataSourceFactory() {
    return Optional.ofNullable(dataSourceFactory);
  }

}
