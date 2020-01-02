package com.malehm.sample.secured.boundary;

import java.util.Set;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ServiceClientIT {

  private static final TokenGenerator tokenGenerator = new TokenGenerator();

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class).addPackages(true, "com.malehm.secured")
        .addAsResource(new ByteArrayAsset(ServiceClientIT.tokenGenerator.getPublicKey().getBytes()),
            "publicKey")
        .addAsResource(
            new ByteArrayAsset(ServiceClientIT.tokenGenerator.getPrivateKey().getBytes()),
            "privateKey")
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Test
  public void test() throws Exception {
    final String jwt =
        ServiceClientIT.tokenGenerator.createToken("service", "me", "someone", Set.of("reader"));
    System.out.println("jwt: " + jwt);
    System.out.println("pk: " + ServiceClientIT.tokenGenerator.getPublicKey());
  }

}
