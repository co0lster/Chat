package chat;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 * Where "localhost" is the address of the host,
 * "EchoChamber" is the name of the package
 * and "echo" is the address to access this class from the server
 */



@ServerEndpoint(value = "/rooms/{room}/user/{user}")
public class EchoServer {



    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was
     * successful.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("room") final String room, @PathParam("user") final String user){

        session.getUserProperties().put("room",room);
        session.getUserProperties().put("user",user);

        SessionHandler.addSession(String.valueOf(session.getId()),session);

        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String json, Session session) {

            for (Map.Entry<String, Session> entry : SessionHandler.openSessions.entrySet()) {
                Session s = entry.getValue();
                if (s.isOpen()) {
                    SessionHandler.sendToRoom(json, session.getUserProperties().get("room").toString());

                }
            }



        // check if session corresponds to the roomn
    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
        SessionHandler.removeSession(String.valueOf(session.getId()),session);
    }
}