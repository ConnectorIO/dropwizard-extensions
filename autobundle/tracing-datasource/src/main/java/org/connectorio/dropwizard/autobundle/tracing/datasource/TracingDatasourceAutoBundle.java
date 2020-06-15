package org.connectorio.dropwizard.autobundle.tracing.datasource;

import brave.http.HttpTracing;
import brave.opentracing.BraveTracer;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import io.opentracing.contrib.jdbc.TracingDriver;
import io.opentracing.util.GlobalTracer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.SimpleBundle;
import org.connectorio.dropwizard.autobundle.datasource.DataSourceAutoBundle;
import org.connectorio.dropwizard.autobundle.tracing.zipkin.ZipkinAutoBundle;

public class TracingDatasourceAutoBundle<T extends Configuration> extends SimpleBundle<T> implements AutomaticBundle<T> {

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    Optional<DataSourceFactory> dataSourceFactory = fetch(DataSourceAutoBundle.class, bundles)
      .map(b -> (DataSourceAutoBundle<T>) b)
      .flatMap(DataSourceAutoBundle::getDataSourceFactory)
      .filter(DataSourceFactory.class::isInstance)
      .map(DataSourceFactory.class::cast);

    Optional<HttpTracing> tracing = fetch(ZipkinAutoBundle.class, bundles)
      .map(b -> (ZipkinAutoBundle<T>) b)
      .flatMap(ZipkinAutoBundle::getHttpTracing);

    if (dataSourceFactory.isPresent() && tracing.isPresent()) {
      GlobalTracer.registerIfAbsent(BraveTracer.create(tracing.get().tracing()));
      dataSourceFactory.get().setDriverClass(TracingDriver.class.getName());
    }
  }

  @Override
  public List<Class<?>> getDependencies() {
    return Arrays.asList(DataSourceAutoBundle.class, ZipkinAutoBundle.class);
  }

}
