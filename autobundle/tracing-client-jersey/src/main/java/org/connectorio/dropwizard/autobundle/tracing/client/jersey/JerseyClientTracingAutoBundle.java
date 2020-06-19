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
package org.connectorio.dropwizard.autobundle.tracing.client.jersey;

import brave.http.HttpTracing;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.SimpleBundle;
import org.connectorio.dropwizard.autobundle.client.jersey.JerseyClientAutoBundle;
import org.connectorio.dropwizard.autobundle.tracing.zipkin.ZipkinAutoBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyClientTracingAutoBundle<T extends Configuration> extends SimpleBundle<T> implements AutomaticBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(JerseyClientTracingAutoBundle.class);

  private JerseyClientBuilder jerseyClientBuilder;

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    Optional<HttpTracing> httpTracing = fetch(ZipkinAutoBundle.class, bundles)
      .flatMap(ZipkinAutoBundle::getHttpTracing);
    Optional<JerseyClientBuilder> clientBuilder = fetch(
      JerseyClientAutoBundle.class, bundles)
      .flatMap(JerseyClientAutoBundle::getJerseyClientBuilder);

    if (!clientBuilder.isPresent()) {
      logger.warn("Jersey client builder is not available, skipping registration of request tracers.");
      return;
    }

    if (!httpTracing.isPresent()) {
      logger.warn("Zipkin integration is not available, disabling client tracing support.");
      this.jerseyClientBuilder = clientBuilder.get();
    } else {
      this.jerseyClientBuilder = new TracedJerseyClientBuilder(environment, clientBuilder.get(), httpTracing.get());
    }
  }

  public Optional<JerseyClientBuilder> getJerseyClientBuilder() {
    return Optional.ofNullable(jerseyClientBuilder);
  }

  @Override
  public List<Class<?>> getDependencies() {
    return Arrays.asList(JerseyClientAutoBundle.class, ZipkinAutoBundle.class);
  }

}
