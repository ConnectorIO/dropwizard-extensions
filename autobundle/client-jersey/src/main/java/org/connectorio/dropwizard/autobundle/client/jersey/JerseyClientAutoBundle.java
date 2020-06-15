package org.connectorio.dropwizard.autobundle.client.jersey;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import java.util.Map;
import java.util.Optional;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyClientAutoBundle<T extends Configuration> implements AutomaticBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(JerseyClientAutoBundle.class);

  private JerseyClientBuilder jerseyClientBuilder;

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    if (configuration instanceof JerseyClientAwareConfiguration) {
      JerseyClientConfiguration clientConfiguration = ((JerseyClientAwareConfiguration) configuration)
        .getJerseyClientConfiguration();
      jerseyClientBuilder = new JerseyClientBuilder(environment).using(clientConfiguration);
    } else {
      logger.warn("Configuration does not ship Jersey client information. Make sure your configuration implements {}",
        JerseyClientAwareConfiguration.class);
    }
  }

  public Optional<JerseyClientBuilder> getJerseyClientBuilder() {
    return Optional.ofNullable(jerseyClientBuilder);
  }

}
