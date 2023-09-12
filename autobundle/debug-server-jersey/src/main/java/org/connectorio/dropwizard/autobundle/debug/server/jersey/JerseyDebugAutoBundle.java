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
package org.connectorio.dropwizard.autobundle.debug.server.jersey;

import io.dropwizard.core.setup.Environment;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.SimpleBundle;
import org.glassfish.jersey.message.internal.TracingLogger.Level;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.TracingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Additional bundle which enables verbose logging of Jersey per configured settings.
 *
 * @param <T> Type of configuration.
 */
public class JerseyDebugAutoBundle<T> extends SimpleBundle<T> implements AutomaticBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(JerseyDebugAutoBundle.class);

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    logger.info("Enabling verbose logging for Jersey");

    String tracingType = option(ServerProperties.TRACING).orElse(TracingConfig.ON_DEMAND.name());
    String tracingThreshold = option(ServerProperties.TRACING_THRESHOLD).orElse(Level.VERBOSE.name());

    environment.jersey().property(ServerProperties.TRACING, tracingType);
    environment.jersey().property(ServerProperties.TRACING_THRESHOLD, tracingThreshold);
  }

  private Optional<String> option(String name) {
    Optional<String> env = Optional.ofNullable(System.getenv(name));
    Optional<String> property = Optional.ofNullable(System.getProperty(name));
    return env.isPresent() ? env : property;
  }

}
