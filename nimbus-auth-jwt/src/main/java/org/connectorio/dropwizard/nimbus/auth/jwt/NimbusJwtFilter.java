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

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;

/**
 * Implementation of custom authn filter attached to
 *
 * @author Łukasz Dywicki
 *
 * @param <T>
 */
public class NimbusJwtFilter<T extends Principal> extends AuthFilter<JWT, T> {

  private static final String DEFAULT_AUTH_SCHEME = "Bearer";

  @Nullable
  private JWT getCredentials(String header) {
    if (header == null) {
      return null;
    }

    final int space = header.indexOf(' ');
    if (space <= 0) {
      return null;
    }

    final String method = header.substring(0, space);
    if (!prefix.equalsIgnoreCase(method)) {
      return null;
    }

    try {
      return JWTParser.parse(header.substring(space + 1));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    final JWT credentials = getCredentials(requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    if (!authenticate(requestContext, credentials, prefix)) {
      throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }
  }

  /**
   * Builder for {@link NimbusJwtFilter}.
   * <p>An {@link Authenticator} must be provided during the building process.</p>
   *
   * @param <P> the type of the principal
   */
  public static class Builder<P extends JwtClaimsSetPrincipal> extends AuthFilterBuilder<JWT, P, NimbusJwtFilter<P>> {

    public Builder() {
      setPrefix(DEFAULT_AUTH_SCHEME);
    }

    @Override
    protected NimbusJwtFilter<P> newInstance() {
      return new NimbusJwtFilter<>();
    }
  }
}
