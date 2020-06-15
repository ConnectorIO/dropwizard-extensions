package org.connectorio.dropwizard.autobundle.jwt;

import io.dropwizard.Configuration;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.setup.Environment;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.connectorio.dropwizard.autobundle.AutomaticBundle;
import org.connectorio.dropwizard.nimbus.auth.jwt.DefaultJwtAuthenticator;
import org.connectorio.dropwizard.nimbus.auth.jwt.JwtClaimsSetAuthenticator;
import org.connectorio.dropwizard.nimbus.auth.jwt.JwtClaimsSetPrincipal;
import org.connectorio.dropwizard.nimbus.auth.jwt.NimbusJwtBundle;
import org.connectorio.dropwizard.nimbus.auth.jwt.NimbusJwtFilter;
import org.connectorio.dropwizard.nimbus.auth.jwt.config.TokenConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTAutoBundle<T extends Configuration, X extends JwtClaimsSetPrincipal> implements AutomaticBundle<T> {

  private final Logger logger = LoggerFactory.getLogger(JWTAutoBundle.class);
  private final ProvisionedJwtBundle<Configuration, TokenConfiguration> delegate;

  public JWTAutoBundle() {
    this.delegate = new ProvisionedJwtBundle<>(this::extract);
  }

  private <T extends TokenConfiguration, C extends Configuration> T extract(C configuration) {
    if (configuration instanceof JWTAwareConfiguration) {
      return (T) ((JWTAwareConfiguration) configuration).getTokenConfiguration();
    }
    return null;
  }

  @Override
  public void run(T configuration, Environment environment, Map<Class<?>, AutomaticBundle<T>> bundles) throws Exception {
    if (configuration instanceof JWTAwareConfiguration) {
      delegate.run(configuration, environment);
    } else {
      logger.warn("Configuration does not ship token information. Make sure your configuration implements {}",
        JWTAwareConfiguration.class);
    }
  }

  public Optional<NimbusJwtFilter<X>> createFilter(JwtClaimsSetAuthenticator<X> authenticator, Authorizer<X> authorizer) {
    return Optional.ofNullable(delegate)
      .map(NimbusJwtBundle::getJwtConfig)
      .map(cfg -> new NimbusJwtFilter.Builder<X>()
        .setAuthenticator(new DefaultJwtAuthenticator<>(authenticator, cfg))
        .setAuthorizer(authorizer)
        .buildAuthFilter()
      );
  }

  static class ProvisionedJwtBundle<C extends Configuration, T extends TokenConfiguration> extends NimbusJwtBundle<C, T> {
    public ProvisionedJwtBundle(Function<C, T> extractor) {
      super(extractor);
    }
  }
}
