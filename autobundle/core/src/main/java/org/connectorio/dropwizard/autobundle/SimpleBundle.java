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

import java.util.Map;
import java.util.Optional;

/**
 * Basic base class to support a most common operation in case of dependency chains - navigating to
 * dependency.
 *
 * @param <T> Type of configuration.
 */
public abstract class SimpleBundle<T> implements AutomaticBundle<T> {

  public <X> Optional<X> fetch(Class<X> bundle, Map<Class<?>, AutomaticBundle<T>> bundles) {
    return Optional.ofNullable(bundles.get(bundle))
      .filter(bundle::isInstance)
      .map(bundle::cast);
  }

}
