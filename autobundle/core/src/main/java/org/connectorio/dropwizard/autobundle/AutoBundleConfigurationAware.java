package org.connectorio.dropwizard.autobundle;

/**
 * Additional, optional configuration for auto bundle which can be used to setup startup order
 * of discovered modules.
 */
public interface AutoBundleConfigurationAware {

  AutoBundleConfiguration getAutoBundleConfiguration();

}
