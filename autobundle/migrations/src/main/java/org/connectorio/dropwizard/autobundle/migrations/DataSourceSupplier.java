package org.connectorio.dropwizard.autobundle.migrations;

import io.dropwizard.db.PooledDataSourceFactory;
import java.util.function.Supplier;

/**
 * A mutable supplier which allows to bring data source from elsewhere.
 */
class DataSourceSupplier implements Supplier<PooledDataSourceFactory> {
  private PooledDataSourceFactory dataSourceFactory;

  public void setDataSourceFactory(PooledDataSourceFactory dataSourceFactory) {
    this.dataSourceFactory = dataSourceFactory;
  }

  @Override
  public PooledDataSourceFactory get() {
    return dataSourceFactory;
  }
}
