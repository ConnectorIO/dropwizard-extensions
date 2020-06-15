package org.connectorio.dropwizard.autobundle.tracing.zipkin;

import com.smoketurner.dropwizard.zipkin.ZipkinFactory;

public interface ZipkinAwareConfiguration {

  ZipkinFactory getZipkinConfiguration();

}
