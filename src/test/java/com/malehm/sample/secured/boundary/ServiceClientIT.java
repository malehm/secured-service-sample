package com.malehm.sample.secured.boundary;

import java.util.Set;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ServiceClientIT {

  private final TokenGenerator tokenGenerator = new TokenGenerator();
  private static final KeyStore KEY_STORE = KeyStore.getInstance();

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class).addPackages(true, "com.malehm.secured")
        .addAsResource(new StringAsset(ServiceClientIT.KEY_STORE.getPublicKey()),
            KeyStore.PUBLIC_KEY_FILE)
        .addAsResource(new StringAsset(ServiceClientIT.KEY_STORE.getPrivateKey()),
            KeyStore.PRIVATE_KEY_FILE)
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  public static void runCli() {
    // your cli goes here
  }

  @Test
  public void test() throws Exception {
    // TODO init tokenGenerator from resources in deployment
    final String jwt =
        this.tokenGenerator.createToken("service", "me", "someone", Set.of("reader"));
    System.out.println("jwt: " + jwt);
    System.out.println("pk: " + KeyStore.fromFile().getPublicKey());
  }

}
