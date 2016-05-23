package message;

import script.Script;

public class Message {
	private Script m_data;
	
	private String m_target;
	
	private int m_type = 0;
	
	/* 消息类型  typed 的值 */
	public final static int MessageType_HertBeat = 0;
	public final static int MessageType_Special = 1;
	public final static int MessageType_Inside = 2;
	
	public Message() {
		this.m_data = null;
		this.m_type = MessageType_HertBeat;
	}
	
	public Message(Script data) {
		this.m_data = data;
		this.m_type = MessageType_Special;
	}
	
	public Message(Script data, int type) {
		this.m_data = data;
		this.m_type = type;
	}
	
	public int getMessageType() {
		return m_type;
	}
	
	public Script getMessageData() {
		return m_data;
	}
	
	public void setTarget(String uid) {
		this.m_target = uid;
	}
	
	public String getTarget() {
		return this.m_target;
	}
	
	public Script getData() {
		return this.m_data;
	}
	
	public int getType() {
		return this.m_type;
	}
}
