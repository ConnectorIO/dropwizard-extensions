package org.connectorio.dropwizard.autobundle.datasource;

import io.dropwizard.db.PooledDataSourceFactory;

public interface DataSourceAwareConfiguration {

  PooledDataSourceFactory getDataSourceFactory();

}
