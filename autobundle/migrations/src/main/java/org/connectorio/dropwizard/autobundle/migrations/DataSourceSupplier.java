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
