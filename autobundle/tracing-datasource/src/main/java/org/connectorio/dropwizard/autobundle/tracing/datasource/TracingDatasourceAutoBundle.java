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
package org.connectorio.dropwizard.autobundle.tracing.datasource;

import brave.http.HttpTracing;
import com.p6spy.engine.spy.P6SpyDriver;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.SimpleBundle;
import org.connectorio.dropwizard.autobundle.datasource.DataSourceAutoBundle;
import org.connectorio.dropwizard.autobundle.tracing.zipkin.ZipkinAutoBundle;

public class TracingDatasourceAutoBundle<T extends Configuration> extends SimpleBundle<T> implements AutomaticBundle<T> {

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    Optional<DataSourceFactory> dataSourceFactory = fetch(DataSourceAutoBundle.class, bundles)
      .map(b -> (DataSourceAutoBundle<T>) b)
      .flatMap(DataSourceAutoBundle::getDataSourceFactory)
      .filter(DataSourceFactory.class::isInstance)
      .map(DataSourceFactory.class::cast);

    Optional<HttpTracing> tracing = fetch(ZipkinAutoBundle.class, bundles)
      .map(b -> (ZipkinAutoBundle<T>) b)
      .flatMap(ZipkinAutoBundle::getHttpTracing);

    if (dataSourceFactory.isPresent() && tracing.isPresent()) {
      DataSourceFactory dataSourceFactoryInstance = dataSourceFactory.get();
      if (dataSourceFactoryInstance.getUrl().startsWith("jdbc:p6spy:")) {
        // automatically override driver class if JDBC uri contains p6spy
        dataSourceFactoryInstance.setDriverClass(P6SpyDriver.class.getName());
      }
    }
  }

  @Override
  public List<Class<?>> getDependencies() {
    return Arrays.asList(DataSourceAutoBundle.class, ZipkinAutoBundle.class);
  }

}
