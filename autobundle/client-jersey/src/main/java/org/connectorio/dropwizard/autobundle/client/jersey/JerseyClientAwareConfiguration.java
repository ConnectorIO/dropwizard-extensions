package org.connectorio.dropwizard.autobundle.client.jersey;

import io.dropwizard.client.JerseyClientConfiguration;

public interface JerseyClientAwareConfiguration {

  JerseyClientConfiguration getJerseyClientConfiguration();

}
