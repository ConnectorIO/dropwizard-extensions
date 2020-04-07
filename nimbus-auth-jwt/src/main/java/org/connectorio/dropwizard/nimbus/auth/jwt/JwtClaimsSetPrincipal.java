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

import java.security.Principal;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * A dedicated kind of principal, backed by a static set of claims coming from JWT token.
 *
 * Because token as a structure is variable most likely you will need to extend this class in order to provide necessary
 * logic related to token extraction.
 *
 * Principal is created after validation of token, so you can assume (till some degree) it is a form of proved identity
 * which you later can use to verify access rights by appropriate security implementation.
 *
 * @author Łukasz Dywicki
 */
public class JwtClaimsSetPrincipal implements Principal {

  protected final JWTClaimsSet claimsSet;

  public JwtClaimsSetPrincipal(JWTClaimsSet claimsSet) {
    this.claimsSet = claimsSet;
  }

  @Override
  public String getName() {
    return claimsSet.getSubject();
  }

}
