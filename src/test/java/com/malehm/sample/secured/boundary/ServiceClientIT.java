package com.malehm.sample.secured.boundary;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServiceClientIT {

  private static KeyPair keyPair;
  private static String pk;

  @BeforeClass
  public static void beforeClass() throws Exception {
    final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048);
    ServiceClientIT.keyPair = kpg.generateKeyPair();
    ServiceClientIT.pk =
        Base64.getEncoder().encodeToString(ServiceClientIT.keyPair.getPublic().getEncoded());
  }

  @Test
  public void test() throws Exception {
    final String jwt = new TokenGenerator().createToken(ServiceClientIT.keyPair);
    System.out.println("jwt: " + jwt);
    System.out.println("pk: " + ServiceClientIT.pk);
  }

}
