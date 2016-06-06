package de.ostfalia.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ServerEndpoint("/chat")
public class WebsocketServer {

	String allMessages = "";
	private static Map<Session, String> clients = 
		    Collections.synchronizedMap(new HashMap<Session, String>());
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config){
		System.out.println("New chat with" + session.getId());
		clients.put(session, "Anonyme Ananas");
		
		//Send new Usernamelist
		try {
      for(Session sessionInstance:clients.keySet()) {
        Collection<String> clientCollection = clients.values();
        Gson gson = new Gson();
        String json = gson.toJson(clientCollection);
        sessionInstance.getBasicRemote().sendText(json);
        System.out.println(json.toString());
      };
    } catch (IOException e) {
      e.printStackTrace();
      }
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
	  JsonElement jelement = new JsonParser().parse(message);
	  JsonObject jobject = jelement.getAsJsonObject();
	  String command = jobject.get("action").getAsString();
		switch (command) {
    case "newUsername":
      clients.put(session, jobject.get("username").getAsString());
      break;
    case "newMessage":
      try {
        for(Session sessionInstance:clients.keySet()) {
          JsonObject jMessage = new JsonObject();
          jMessage.addProperty("action", "message");
          jMessage.addProperty("user", clients.get(session));
          jMessage.addProperty("message", jobject.get("newMessage").getAsString());
          sessionInstance.getBasicRemote()
          .sendText(jMessage.getAsString());
        };
      } catch (IOException e) {
        e.printStackTrace();
        }
      break;
    default:
      break;
    }
		
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		clients.remove(session);
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		clients.remove(session);
		System.out.println("This should never happen!");
	}
	
	
	
}
