package org.connectorio.dropwizard.autobundle;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AutoBundleConfiguration {

  @Valid
  @NotNull
  @JsonProperty("startup")
  private List<String> startup;

  public AutoBundleConfiguration() {}

  public AutoBundleConfiguration(@Valid @NotNull List<String> startup) {
    this.startup = startup;
  }

  public List<String> getStartup() {
    return startup;
  }

}
