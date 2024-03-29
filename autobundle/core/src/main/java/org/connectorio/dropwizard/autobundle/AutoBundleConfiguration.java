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
package org.connectorio.dropwizard.autobundle;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

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
