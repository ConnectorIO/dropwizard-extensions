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

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import org.connectorio.dropwizard.autobundle.graph.BundleNode;
import org.connectorio.dropwizard.autobundle.graph.DependencyGraph;
import org.connectorio.dropwizard.autobundle.graph.Node;
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
    List<BundleNode<T>> startOrder = dependencyMap.sort();
    for (BundleNode<T> bundleNode : startOrder) {
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

}
