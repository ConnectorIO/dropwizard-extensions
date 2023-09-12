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
package org.connectorio.dropwizard.autobundle;

import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import org.connectorio.dropwizard.autobundle.graph.BundleNode;
import org.connectorio.dropwizard.autobundle.graph.DependencyGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A basic mechanism which allows to discover bundles from classpath and get them initialized and
 * run automatically.
 *
 * @param <T> Type of configuration.
 */
public class AutoBundle<T extends Configuration> implements ConfiguredBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(AutoBundle.class);
  private final Application<T> application;

  private DependencyGraph<T> dependencyMap = new DependencyGraph<>();
  private Map<Class<?>, AutomaticBundle<T>> bundleMap = new HashMap<>();

  public AutoBundle(Application<T> application) {
    this.application = application;
  }

  @Override
  public void run(T configuration, Environment environment) throws Exception {
    List<BundleNode<T>> startOrder;
    if (configuration instanceof AutoBundleConfigurationAware) {
      AutoBundleConfiguration bundleConfiguration = ((AutoBundleConfigurationAware) configuration).getAutoBundleConfiguration();

      logger.info("Validating provided startup order of bundles.");
      StringBuilder configDump = new StringBuilder("autoBundles:\n").append("\tstartup:\n");

      startOrder = bundleConfiguration.getStartup().stream()
        .peek(bundle -> configDump.append("\t\t- ").append(bundle).append("\n"))
        .map(this::resolveClass)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(value -> {
          BundleNode<T> bundle = dependencyMap.get(value);

          if (bundle == null) {
            throw new IllegalArgumentException("Bundle " + value + " not found. Configuration is invalid");
          }
          return bundle;
        })
        .collect(Collectors.toList());

      if (logger.isInfoEnabled()) {
        logger.info("Startup order of bundles\n{}", configDump);
      }
    } else {
      startOrder= dependencyMap.sort();
      logger.info("Starting bundles in automatically determined order: {}", startOrder);
    }

    for (BundleNode<T> bundleNode : startOrder) {
      logger.debug("Starting bundle {}", bundleNode.getType().getName());
      bundleNode.get().run(configuration, environment, bundleMap);
    }
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void initialize(Bootstrap<?> bootstrap) {
    ServiceLoader<AutomaticBundle> loadedBundles = ServiceLoader.load(AutomaticBundle.class, bootstrap.getClassLoader());

    for (AutomaticBundle<?> bundle : loadedBundles) {
      logger.info("Bootstrapping automatically discovered bundle {}", bundle.getClass().getName());

      if (bundle instanceof ApplicationAware) {
        ((ApplicationAware<T>) bundle).setApplication(application);
      }

      bundle.initialize(bootstrap);

      dependencyMap.put((AutomaticBundle<T>) bundle);
      bundleMap.put(bundle.getClass(), (AutomaticBundle<T>) bundle);
    }
  }

  public <X> Optional<X> getBundle(Class<X> bundleType) {
    return Optional.ofNullable(bundleMap.get(bundleType))
      .filter(bundleType::isInstance)
      .map(bundleType::cast);
  }

  private Optional<Class<?>> resolveClass(String bundle) {
    try {
      return Optional.of(Class.forName(bundle, false, getClass().getClassLoader()));
    } catch (ClassNotFoundException e) {
      return Optional.empty();
    }
  }

}
