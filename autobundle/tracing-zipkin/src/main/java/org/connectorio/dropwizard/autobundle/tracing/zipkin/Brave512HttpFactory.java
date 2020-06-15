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

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.http.HttpTracing;
import brave.jersey.server.TracingApplicationEventListener;
import brave.propagation.ThreadLocalCurrentTraceContext;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.smoketurner.dropwizard.zipkin.HttpZipkinFactory;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import java.util.Optional;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

/**
 * A custom brave/zipkin reporter factory which is there to fix issues caused by method signature
 * updates. Probably to be removed over time when dropwizard-zipkin module we depend on gets updated
 * to latest versions of libs.
 */
@JsonTypeName("brave512")
public class Brave512HttpFactory extends HttpZipkinFactory {

  protected Optional<HttpTracing> buildTracing(final Environment environment, final Reporter<Span> reporter) {

    final Tracing tracing =
      Tracing.newBuilder()
        .currentTraceContext(
          ThreadLocalCurrentTraceContext.newBuilder()
            .addScopeDecorator(MDCScopeDecorator.get())
            .build())
        .localIp(getServiceHost())
        .localPort(getServicePort())
        .spanReporter(reporter)
        .localServiceName(getServiceName())
        .sampler(getSampler())
        .traceId128Bit(getTraceId128Bit())
        .build();

    final HttpTracing httpTracing =HttpTracing.newBuilder(tracing)
      .clientParser(getClientParser())
      .clientSampler(getClientSampler())
      .serverParser(getServerParser())
      .serverSampler(getServerSampler())
      .build();

    // Register the tracing feature for client and server requests
    environment.jersey().register(TracingApplicationEventListener.create(httpTracing));
    environment.lifecycle().manage(new Managed() {
      @Override
      public void start() throws Exception {
        // nothing to start
      }
      @Override
      public void stop() throws Exception {
        tracing.close();
      }
    });

    return Optional.of(httpTracing);
  }

}
