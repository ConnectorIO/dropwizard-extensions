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
package org.connectorio.dropwizard.autobundle.tracing.zipkin;

import brave.http.HttpTracing;
import com.smoketurner.dropwizard.zipkin.ZipkinBundle;
import com.smoketurner.dropwizard.zipkin.ZipkinFactory;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.ApplicationAware;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;

public class ZipkinAutoBundle<T extends Configuration> implements AutomaticBundle<T>, ApplicationAware<T> {

  private ZipkinBundle<T> delegate;
  private Application<T> application;

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    this.delegate.run(configuration, environment);
  }

  @Override
  public void initialize(Bootstrap<?> bootstrap) {
    this.delegate = new ConfiguredZipkinBundle<>(application.getName());
    this.delegate.initialize(bootstrap);
  }

  @Override
  public List<Class<?>> getDependencies() {
    return Collections.emptyList();
  }

  @Override
  public void setApplication(Application<T> application) {
    this.application = application;
  }

  public Optional<HttpTracing> getHttpTracing() {
    return delegate.getHttpTracing();
  }

  static class ConfiguredZipkinBundle<C extends Configuration> extends ZipkinBundle<C> {
    public ConfiguredZipkinBundle(String serviceName) {
      super(serviceName);
    }

    @Override
    public ZipkinFactory getZipkinFactory(C configuration) {
      if (configuration instanceof ZipkinAwareConfiguration) {
        return ((ZipkinAwareConfiguration) configuration).getZipkinConfiguration();
      }

      throw new IllegalStateException("Passed configuration does not ship zipkin configuration.");
    }
  }

}
