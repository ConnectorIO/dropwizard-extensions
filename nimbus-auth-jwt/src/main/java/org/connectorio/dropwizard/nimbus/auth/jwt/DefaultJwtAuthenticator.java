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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

/**
 * Default implementation of {@link Authenticator} which handles a most repeatable part, namely token validation against
 * signatures and keys stored in remote Json Web Key Set.
 *
 * Present implementation does no rely on Nimbus's {@link com.nimbusds.jwt.proc.JWTClaimsSetVerifier} API, rather uses
 * decoration and delegation of calls to next implementation of {@link JwtClaimsSetAuthenticator}.
 * TODO: consider built-in support for audience verification since its quite standard feature.
 *
 * @author Łukasz Dywicki
 *
 * @param <P> Type of principal required
 */
public class DefaultJwtAuthenticator<P extends JwtClaimsSetPrincipal> implements Authenticator<JWT, P> {

  private final JwtClaimsSetAuthenticator<P> delegate;
  private final URL jwkSetUrl;
  private final RemoteJWKSet<SecurityContext> jwkSource;
  private final JWSVerificationKeySelector<SecurityContext> jwsKeySelector;
  private final DefaultJWTProcessor<SecurityContext> processor;

  public DefaultJwtAuthenticator(JwtClaimsSetAuthenticator<P> delegate, String jwkSetUrl, String signatureAlgorithm) {
    this(delegate, jwkSetUrl, JWSAlgorithm.parse(signatureAlgorithm));
  }

  public DefaultJwtAuthenticator(JwtClaimsSetAuthenticator<P> delegate, String jwkSetUrl, JWSAlgorithm signatureAlgorithm) {
    this(delegate, url(jwkSetUrl), signatureAlgorithm);
  }

  private static URL url(String url) {
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Could not parse JWK URL", e);
    }
  }

  public DefaultJwtAuthenticator(JwtClaimsSetAuthenticator<P> delegate, URL jwkSetUrl, JWSAlgorithm signatureAlgorithm) {
    this.delegate = delegate;
    this.jwkSetUrl = jwkSetUrl;

    ResourceRetriever jwkSetRetriever = new DefaultResourceRetriever(30000, 30000);

    jwkSource = new RemoteJWKSet<>(this.jwkSetUrl, jwkSetRetriever);
    jwsKeySelector = new JWSVerificationKeySelector<>(signatureAlgorithm, jwkSource);

    DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
    jwtProcessor.setJWSKeySelector(jwsKeySelector);
    jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>());

    processor = jwtProcessor;
  }

  @Override
  public Optional<P> authenticate(JWT credentials) throws AuthenticationException {
    try {
      JWTClaimsSet process = processor.process(credentials, null);
      return delegate.authenticate(process);
    } catch (BadJOSEException e) {
      e.printStackTrace();
    } catch (JOSEException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

}
