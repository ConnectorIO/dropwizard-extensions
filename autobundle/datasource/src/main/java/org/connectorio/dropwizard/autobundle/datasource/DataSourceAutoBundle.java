package org.connectorio.dropwizard.autobundle.datasource;

import io.dropwizard.Configuration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.setup.Environment;
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
