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

import io.dropwizard.core.Configuration;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.SimpleBundle;
import org.connectorio.dropwizard.autobundle.datasource.DataSourceAutoBundle;

public class MigrationsAutoBundle<T extends Configuration> extends SimpleBundle<T> implements AutomaticBundle<T> {

  private final MigrationsBundle<T> delegate = new AutoDataSourceMigrationsBundle<>();

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    // migrations are not runnable, hence there is no logic here.
  }

  @Override
  public void initialize(Bootstrap<?> bootstrap) {
    delegate.initialize(bootstrap);
  }

  @Override
  public List<Class<?>> getDependencies() {
    return Arrays.asList(DataSourceAutoBundle.class);
  }

}
