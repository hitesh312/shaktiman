package com.mcbc.shaktiman.game;

import com.mcbc.shaktiman.common.DbOps;
import com.mcbc.shaktiman.common.User;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Produces("text/json")
public class GameManagerEndpoint {

    @Context  //injected response proxy supporting multiple threads
    private HttpServletResponse response;

    GameEngine gameEngine = GameEngine.getGameEngine();


    @GET
    @Path("/login")
    public User login(@QueryParam("userid") String userid,
                      @QueryParam("passHash") String passHash) throws IOException {
        if (!DbOps.verifyLogin(userid, passHash)) {
            response.sendError(Response.Status.FORBIDDEN.getStatusCode(), "Userid / password incorrect");
            return null;
        } else {
            String sessionId = gameEngine.createSession(userid);
            response.setHeader("Session", sessionId);
            return DbOps.getUser(userid);
        }
    }

    @GET
    @Path("/newUser")
    public void newUser(@QueryParam("userid") String userid,
                        @QueryParam("passHash") String passHash,
                        @QueryParam("name") String name,
                        @QueryParam("email") String email) throws IOException {
        try {
            DbOps.addUser(new User(userid, name, email, passHash));
        } catch (Exception ex) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), ex.getMessage());
        }
    }

    @GET
    @Path("/updateUser")
    public void updateUser(@QueryParam("userid") String userid,
                           @QueryParam("passHash") String passHash,
                           @QueryParam("name") String name,
                           @QueryParam("email") String email,
                           @QueryParam("sessionKey") String sessionKey) throws IOException {
        try {
            gameEngine.verifySession(userid, sessionKey);
            DbOps.updateUser(new User(userid, name, email, passHash));
        } catch (Exception ex) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), ex.getMessage());
        }
    }

    @GET
    @Path("/deleteUser")
    public void deleteUser(@QueryParam("userid") String userid,
                           @QueryParam("sessionKey") String sessionKey) throws IOException {
        try {
            gameEngine.verifySession(userid, sessionKey);
            DbOps.deleteUser(userid);
        } catch (Exception ex) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), ex.getMessage());
        }
    }

    @GET
    @Path("/newGame")
    public String newGame(@QueryParam("userid") String userid,
                          @QueryParam("sessionKey") String sessionKey) throws IOException {
        try {
            gameEngine.verifySession(userid, sessionKey);
            return gameEngine.newGame();
        } catch (Exception ex) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), ex.getMessage());
            return null;
        }
    }

    @GET
    @Path("/joinGame")
    public void joinGame(@QueryParam("userid") String userid,
                         @QueryParam("sessionKey") String sessionKey,
                         @QueryParam("gameID") String gameID) throws IOException {
        try {
            gameEngine.verifySession(userid, sessionKey);
            gameEngine.joinGame(gameID);
        } catch (Exception ex) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), ex.getMessage());
        }
    }

    @GET
    @Path("/joinRandomGame")
    public String joinRandomGame(@QueryParam("userid") String userid,
                                 @QueryParam("sessionKey") String sessionKey) throws IOException {
        try {
            gameEngine.verifySession(userid, sessionKey);
            return gameEngine.joinRandomGame();
        } catch (Exception ex) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(), ex.getMessage());
            return null;
        }
    }
}
