package com.malehm.sample.secured.boundary;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.apache.commons.io.IOUtils;

public class KeyStore {

  public static final String PUBLIC_KEY_FILE = "publicKey";
  public static final String PRIVATE_KEY_FILE = "privateKey";

  private final KeyPair keyPair;

  private KeyStore(final KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  public static KeyStore getInstance() {
    try {
      final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
      kpg.initialize(2048);
      return new KeyStore(kpg.generateKeyPair());
    } catch (final Exception ex) {
      throw new RuntimeException("Exception creating KeyStore", ex);
    }
  }

  public static KeyStore fromFile() {
    try {

      final String publicKeyContent = IOUtils.toString(
          KeyPair.class.getClassLoader().getResourceAsStream(KeyStore.PUBLIC_KEY_FILE),
          StandardCharsets.UTF_8);
      final String privateKeyContent = IOUtils.toString(
          KeyPair.class.getClassLoader().getResourceAsStream(KeyStore.PRIVATE_KEY_FILE),
          StandardCharsets.UTF_8);

      final KeyFactory kf = KeyFactory.getInstance("RSA");

      final PKCS8EncodedKeySpec keySpecPKCS8 =
          new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
      final PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);

      final X509EncodedKeySpec keySpecX509 =
          new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
      final PublicKey publicKey = kf.generatePublic(keySpecX509);
      return new KeyStore(new KeyPair(publicKey, privateKey));
    } catch (final Exception ex) {
      throw new RuntimeException("Exception creating KeyStore", ex);
    }
  }

  public String getPublicKey() {
    return this.toString(this.getKeyPair().getPublic());
  }

  public String getPrivateKey() {
    return this.toString(this.getKeyPair().getPrivate());
  }

  private String toString(final Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  public KeyPair getKeyPair() {
    return keyPair;
  }

}
