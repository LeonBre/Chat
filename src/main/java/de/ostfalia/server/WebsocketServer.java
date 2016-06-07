package de.ostfalia.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
		addUser(session, "Anonyme Ananas");
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
	  System.out.println(message);
	  JsonElement jelement = new JsonParser().parse(message);
	  JsonObject jobject = jelement.getAsJsonObject();
	  String command = jobject.get("action").getAsString();
		switch (command) {
    case "newUsername":
      addUser(session, jobject.get("username").getAsString());
      break;
    case "newMessage":
      sendNewMessage(clients.get(session), jobject);
      break;
    default:
      break;
    }
		
	}
	
	public void addUser(Session session, String username) {
	  clients.put(session, username);
	  sendUpdateUsername();
	}
	
	private void sendNewMessage(String username, JsonObject jobject) {
	  try {
      for(Session sessionInstance:clients.keySet()) {
        JsonObject jMessage = new JsonObject();
        jMessage.addProperty("action", "message");
        jMessage.addProperty("user", username);
        System.out.println(jMessage.toString());
        jMessage.addProperty("message", jobject.get("newMessage").getAsString());
        sessionInstance.getBasicRemote()
        .sendText(jMessage.toString());
      };
    } catch (IOException e) {
      e.printStackTrace();
      }
	}
	
	private void sendUpdateUsername() {
	  try {
      for(Session sessionInstance:clients.keySet()) {
        JsonObject userUpdate = new JsonObject();
        userUpdate.addProperty("action", "userUpdate");
        JsonArray userArray = new JsonArray();
        for (String user : clients.values()) {
          JsonObject userObject = new JsonObject();
          userObject.addProperty("username", user);
          userArray.add(userObject); 
        }
        userUpdate.add("userList", userArray);
        System.out.println(userUpdate.toString());
        sessionInstance.getBasicRemote().sendText(userUpdate.toString());
      }
      } catch (IOException e) {
        e.printStackTrace();
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
		throwable.printStackTrace();
	}
	
	
	
}
