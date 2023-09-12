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
package org.connectorio.dropwizard.autobundle.client.jersey;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyClientAutoBundle<T extends Configuration> implements AutomaticBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(JerseyClientAutoBundle.class);

  private JerseyClientBuilder jerseyClientBuilder;

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    if (configuration instanceof JerseyClientAwareConfiguration) {
      JerseyClientConfiguration clientConfiguration = ((JerseyClientAwareConfiguration) configuration)
        .getJerseyClientConfiguration();
      jerseyClientBuilder = new JerseyClientBuilder(environment).using(clientConfiguration);
    } else {
      logger.warn("Configuration does not ship Jersey client information. Make sure your configuration implements {}",
        JerseyClientAwareConfiguration.class);
    }
  }

  public Optional<JerseyClientBuilder> getJerseyClientBuilder() {
    return Optional.ofNullable(jerseyClientBuilder);
  }

}
