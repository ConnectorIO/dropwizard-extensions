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

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Environment;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Marker interface for a bundle which is discoverable via classpath scanning.
 *
 * @param <T> Type of configuration.
 */
public interface AutomaticBundle<T> extends ConfiguredBundle<T> {

  @Override
  default void run(T configuration, Environment environment) throws Exception {
    run(configuration, environment, Collections.emptyMap());
  }

  /**
   * A variant of run method which allows bundle fetch other pre-initialized bundles.
   *
   * This is an helper for cases where one bundle depends on another. Initialized bundles should
   * contain initialized and running bundles pointed via supplied {@link #getDependencies()}.
   *
   * @param configuration    the configuration object
   * @param environment      the application's {@link Environment}
   * @param bundles Bundles which are already running.
   * @throws Exception if something goes wrong
   */
  void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception;

  /**
   * Bundle dependencies which are needed by the bundle.
   * There is no type boundary on this method, however returned dependencies should be other
   * automatic bundles.
   *
   * @return Dependencies of this bundle.
   */
  default List<Class<?>> getDependencies() {
    return Collections.emptyList();
  }

}
