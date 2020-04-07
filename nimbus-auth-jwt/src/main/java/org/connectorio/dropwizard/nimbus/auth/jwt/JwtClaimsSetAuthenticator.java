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

import com.nimbusds.jwt.JWTClaimsSet;

import io.dropwizard.auth.Authenticator;

/**
 * Dedicated authenticator taking JWT token claims and returning {@link JwtClaimsSetPrincipal}.
 * It is a marker interface for later implementation by users who need to provide own authentication logic. A common
 * use case is verification of token audience ("aud" claim/attribute in the token) within the service receiving request.
 *
 * @author Łukasz Dywicki
 *
 * @param <P> Principal kind, type to which claims set is mapped.
 */
public interface JwtClaimsSetAuthenticator<P extends JwtClaimsSetPrincipal> extends Authenticator<JWTClaimsSet, P> {

}
