package player;

import java.util.UUID;

import message.Message;
import module.Module;

import server.Server;
import server.ServerManager;
import session.Session;

public class Player extends Module {
	private Server m_socket;
	
	private String m_userName;
	
	private String m_uuid;
	
	public Player() {
		m_uuid = UUID.randomUUID().toString();
	}
	
	public Player(Server socket) {
		this();
		m_socket = socket;
	}
	
	public String getUUid() {
		return m_uuid;
	}
	
	public String getUserName() {
		return m_userName;
	}
	
	public Server getSocket() {
		return m_socket;
	}
	
	private void submitMessage(Message msg) {
		Session.submitMessage(msg);
	}
	
	public void putMessage(Message msg) {
		msg.setTarget(m_uuid);
		submitMessage(msg);
	}
	
	public void putMessage(Message msg, String recUuid) {
		msg.setTarget(recUuid);
		submitMessage(msg);
	}
}
