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
package org.connectorio.dropwizard.nimbus.auth.jwt;

import java.util.function.Function;
import org.connectorio.dropwizard.nimbus.auth.jwt.config.JwtConfiguration;
import org.connectorio.dropwizard.nimbus.auth.jwt.config.TokenConfiguration;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Environment;

/**
 * A basic bundle which allows to group configuration options for token processing in a separate class.
 *
 * @author Łukasz Dywicki
 *
 * @param <C> Type of configuration.
 * @param <J> Type of token configuration.
 */
public class NimbusJwtBundle<C extends Configuration, J extends TokenConfiguration> implements ConfiguredBundle<C> {

    private final Function<C, J> extractor;
    private JwtConfiguration jwtConfig;

    public NimbusJwtBundle(Function<C, J> extractor) {
        this.extractor = extractor;
    }

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        J tokenConfig = extractor.apply(configuration);

        jwtConfig = tokenConfig.getJwtConfiguration();
    }

    public JwtConfiguration getJwtConfig() {
        return jwtConfig;
    }

}
