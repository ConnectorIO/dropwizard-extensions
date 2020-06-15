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
package org.connectorio.dropwizard.camel.hk2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.camel.spi.BeanRepository;
import org.glassfish.jersey.internal.inject.InjectionManager;

/**
 * A bean repository which allows to look up bean by their type thus bind Jersey HK2 supplied services to Camel managed
 * components.
 *
 * @author Łukasz Dywicki
 */
public class DropwizardBeanRepository implements BeanRepository {

  private final InjectionManager injectionManager;

  public DropwizardBeanRepository(InjectionManager injectionManager) {
    this.injectionManager = injectionManager;
  }

  @Override
  public Object lookupByName(String name) {
    return null;
  }

  @Override
  public <T> T lookupByNameAndType(String name, Class<T> type) {
    return null;
  }

  @Override
  public <T> Map<String, T> findByTypeWithName(Class<T> type) {
    return null;
  }

  @Override
  public <T> Set<T> findByType(Class<T> type) {
    return new HashSet<>(injectionManager.getAllInstances(type));
  }
}
