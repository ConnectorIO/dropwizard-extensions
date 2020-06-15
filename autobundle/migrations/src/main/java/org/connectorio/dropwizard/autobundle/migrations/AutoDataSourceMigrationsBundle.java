package org.connectorio.dropwizard.autobundle.migrations;

import io.dropwizard.Configuration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import java.util.function.Supplier;
import org.connectorio.dropwizard.autobundle.datasource.DataSourceAwareConfiguration;

/**
 * An extension of migrations bundle which is feed by data source supplier.
 *
 * @param <T> Type of configuration object.
 */
class AutoDataSourceMigrationsBundle<T extends Configuration> extends MigrationsBundle<T> {

  @Override
  public PooledDataSourceFactory getDataSourceFactory(T configuration) {
    if (configuration instanceof DataSourceAwareConfiguration) {
      return ((DataSourceAwareConfiguration) configuration).getDataSourceFactory();
    }

    throw new IllegalArgumentException("Application configuration does not ship database connection information");
  }
}
