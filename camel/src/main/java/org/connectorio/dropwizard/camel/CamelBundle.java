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
package org.connectorio.dropwizard.camel;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Map;
import java.util.Objects;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultRegistry;
import org.connectorio.dropwizard.camel.hk2.MapPropertiesSource;
import org.connectorio.dropwizard.camel.hk2.MapRepository;

/**
 * Defines camel bundle - an attachable configuration and runtime unit from Dropwizard point of view.
 *
 * @param <T> Type of configuration.
 *
 * @author Łukasz Dywicki
 */
public abstract class CamelBundle<T extends CamelConfiguration> implements ConfiguredBundle<T> {

  private DefaultCamelContext context;

  @Override
  public void run(T configuration, Environment environment) throws Exception {
    context = new DefaultCamelContext();
    Map<String, Object> properties = configuration.getProperties();
    context.getPropertiesComponent().addPropertiesSource(new MapPropertiesSource(properties));
    context.setRegistry(new DefaultRegistry(new MapRepository(getBeans(properties))));

    /* Tracing ... not working yet.
    OpenTracingTracer tracer = new OpenTracingTracer();
    MetricsRoutePolicyFactory routePolicyFactory = new MetricsRoutePolicyFactory();
    routePolicyFactory.setMetricsRegistry(environment.metrics());
    context.addRoutePolicyFactory(routePolicyFactory);
    context.addRoutePolicyFactory(tracer);
   */
    //environment.jersey().register(DropwizardInjector.class);
    environment.lifecycle().manage(new CamelDropwizardLifecycle(context));
  }

  @Override
  public void initialize(Bootstrap<?> bootstrap) {
  }

  public CamelContext getContext() {
    return Objects.requireNonNull(context);
  }

  protected abstract Map<String, Object> getBeans(Map<String, Object> properties);

}
