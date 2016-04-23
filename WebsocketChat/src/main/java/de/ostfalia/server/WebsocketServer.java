package de.ostfalia.server;

import java.io.IOException;
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

@ServerEndpoint("/chat")
public class WebsocketServer {

	String allMessages = "";
	private static Map<Session, String> clients = 
		    Collections.synchronizedMap(new HashMap<Session, String>());
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig config){
		System.out.println("New chat with" + session.getId());
		clients.put(session, "Anonyme Ananas");
		
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
		if(message.contains("Username:")){
			
			clients.put(session, message.split(":")[1]);
		}else{
		
		try {
			for(Session sessionInstance:clients.keySet()) {
				sessionInstance.getBasicRemote().sendText(clients.get(session) + " : " + message);
			};
		} catch (IOException e) {
			e.printStackTrace();
			}
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
