package org.connectorio.dropwizard.autobundle.migrations;

import io.dropwizard.Configuration;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.autobundle.SimpleBundle;
import org.connectorio.dropwizard.autobundle.datasource.DataSourceAutoBundle;

public class MigrationsAutoBundle<T extends Configuration> extends SimpleBundle<T> implements AutomaticBundle<T> {

  private final MigrationsBundle<T> delegate = new AutoDataSourceMigrationsBundle<>();

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    // migrations are not runnable, hence there is no logic here.
  }

  @Override
  public void initialize(Bootstrap<?> bootstrap) {
    delegate.initialize(bootstrap);
  }

  @Override
  public List<Class<?>> getDependencies() {
    return Arrays.asList(DataSourceAutoBundle.class);
  }

}
