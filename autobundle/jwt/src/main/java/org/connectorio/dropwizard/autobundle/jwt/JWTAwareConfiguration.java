package org.connectorio.dropwizard.autobundle.jwt;

import org.connectorio.dropwizard.nimbus.auth.jwt.config.TokenConfiguration;

public interface JWTAwareConfiguration {

  TokenConfiguration getTokenConfiguration();

}
