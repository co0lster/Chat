package chat;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SessionHandler {

    private static final Set<Session> sessions = new HashSet<>();
    static Map<String, Session> openSessions = new HashMap<>();


    public static void addSession(String sessionId, Session session) {
        openSessions.put(sessionId,session);
        sessions.add(session);

    }

    public static void removeSession(String sessionId, Session session) {
        sessions.remove(session);
        openSessions.remove(sessionId,session);


    }

    public static void sendToSession (Session session, String message){

        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendToRoom(String message, String room){
        sessions.forEach( x -> {
            if(x.getUserProperties().containsValue(room)) {
                try {
                    x.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void sendToSessionExceptMe(String message, String me){
        sessions.forEach(x -> {
            try {
                if(x.getUserProperties().containsValue(me))
                x.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }



}
