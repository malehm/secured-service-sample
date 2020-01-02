package com.malehm.sample.secured.boundary;

import java.security.KeyPair;
import java.util.Set;
import java.util.UUID;
import org.keycloak.TokenIdGenerator;
import org.keycloak.common.util.Time;
import org.keycloak.representations.AccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class TokenGenerator {

  private static String ISSUER = "http://localhost:8080/auth/realms/application";
  private final KeyStore keyStore;

  public TokenGenerator() {
    this.keyStore = KeyStore.fromFile();
  }

  public String createToken(final AccessToken token) {
    return this.createToken(this.keyStore.getKeyPair(), token);
  }

  public String createToken(final String resource, final String issuedFor, final String username,
      final Set<String> roles) {
    final AccessToken accessToken =
        this.createAccessToken(resource, issuedFor, resource, roles, username);
    return this.createToken(this.keyStore.getKeyPair(), accessToken);
  }

  private AccessToken createAccessToken(final String audience, final String issuedFor,
      final String resource, final Set<String> roles, final String preferredUsername) {
    final AccessToken jwt = new AccessToken().id(TokenIdGenerator.generateId())
        .expiration(Time.currentTime() + 10).notBefore(0);
    jwt.setSessionState(UUID.randomUUID().toString());
    jwt.setAcr("1");
    jwt.addAccess(resource).roles(roles);
    jwt.setPreferredUsername(preferredUsername);

    jwt.issuedNow().issuer(TokenGenerator.ISSUER).addAudience(audience)
        .subject(UUID.randomUUID().toString()).type("Bearer").issuedFor(issuedFor);
    return jwt;
  }

  private String createToken(final KeyPair pair, final AccessToken accessToken) {
    try {
      final JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("jwt.key")
          .type(JOSEObjectType.JWT).build();
      final JWTClaimsSet claimsSet =
          JWTClaimsSet.parse(new ObjectMapper().writeValueAsString(accessToken));
      final SignedJWT signedJWT = new SignedJWT(header, claimsSet);
      final JWSSigner signer = new RSASSASigner(pair.getPrivate());
      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (final Exception ex) {
      throw new RuntimeException("Exception creating token", ex);
    }
  }

}
