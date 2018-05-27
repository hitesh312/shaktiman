package com.mcbc.shaktiman.game;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Produces("text/json")
public class GameManagerEndpoint {

    @GET
    @Path("/greet")
    public String greet(){
        return "hello world";
    }
}
