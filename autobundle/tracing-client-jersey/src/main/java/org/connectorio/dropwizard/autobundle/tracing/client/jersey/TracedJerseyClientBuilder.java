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
import brave.jaxrs2.TracingClientFilter;
import com.codahale.metrics.httpclient.HttpClientMetricNameStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.HostnameVerifier;
import javax.validation.Validator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.RxInvokerProvider;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.config.Registry;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.glassfish.jersey.client.spi.ConnectorProvider;

class TracedJerseyClientBuilder extends JerseyClientBuilder {

  private final JerseyClientBuilder delegate;
  private HttpTracing httpTracing;

  public TracedJerseyClientBuilder(Environment environment, JerseyClientBuilder clientBuilder, HttpTracing httpTracing) {
    super(environment);
    this.delegate = clientBuilder;
    this.httpTracing = httpTracing;
  }

  @Override
  public void setApacheHttpClientBuilder(HttpClientBuilder apacheHttpClientBuilder) {
    delegate.setApacheHttpClientBuilder(apacheHttpClientBuilder);
  }

  @Override
  public JerseyClientBuilder withProvider(Object provider) {
    return delegate.withProvider(provider);
  }

  @Override
  public JerseyClientBuilder withProvider(Class<?> klass) {
    return delegate.withProvider(klass);
  }

  @Override
  public JerseyClientBuilder withProperty(String propertyName, Object propertyValue) {
    return delegate.withProperty(propertyName, propertyValue);
  }

  @Override
  public JerseyClientBuilder using(JerseyClientConfiguration configuration) {
    return delegate.using(configuration);
  }

  @Override
  public JerseyClientBuilder using(Environment environment) {
    return delegate.using(environment);
  }

  @Override
  public JerseyClientBuilder using(Validator validator) {
    return delegate.using(validator);
  }

  @Override
  public JerseyClientBuilder using(ExecutorService executorService, ObjectMapper objectMapper) {
    return delegate.using(executorService, objectMapper);
  }

  @Override
  public JerseyClientBuilder using(ExecutorService executorService) {
    return delegate.using(executorService);
  }

  @Override
  public JerseyClientBuilder using(ObjectMapper objectMapper) {
    return delegate.using(objectMapper);
  }

  @Override
  public JerseyClientBuilder using(ConnectorProvider connectorProvider) {
    return delegate.using(connectorProvider);
  }

  @Override
  public JerseyClientBuilder using(HttpRequestRetryHandler httpRequestRetryHandler) {
    return delegate.using(httpRequestRetryHandler);
  }

  @Override
  public JerseyClientBuilder using(DnsResolver resolver) {
    return delegate.using(resolver);
  }

  @Override
  public JerseyClientBuilder using(HostnameVerifier verifier) {
    return delegate.using(verifier);
  }

  @Override
  public JerseyClientBuilder using(Registry<ConnectionSocketFactory> registry) {
    return delegate.using(registry);
  }

  @Override
  public JerseyClientBuilder using(HttpClientMetricNameStrategy metricNameStrategy) {
    return delegate.using(metricNameStrategy);
  }

  @Override
  public JerseyClientBuilder name(String environmentName) {
    return delegate.name(environmentName);
  }

  @Override
  public JerseyClientBuilder using(HttpRoutePlanner routePlanner) {
    return delegate.using(routePlanner);
  }

  @Override
  public JerseyClientBuilder using(CredentialsProvider credentialsProvider) {
    return delegate.using(credentialsProvider);
  }

  @Override
  public JerseyClientBuilder using(ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy) {
    return delegate.using(serviceUnavailableRetryStrategy);
  }

  @Override
  public <RX extends RxInvokerProvider<?>> Client buildRx(String name, Class<RX> invokerType) {
    return delegate.buildRx(name, invokerType);
  }

  @Override
  public Client build(String name) {
    Client client = delegate.build(name);
    client.register(TracingClientFilter.create(httpTracing));
    return client;
  }
}
