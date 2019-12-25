package com.malehm.sample.secured.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("ping")
public class PingResource {

  @GET
  public String pong() {
    return "pong";
  }

}
