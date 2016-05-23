package session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import player.Player;
import player.PlayerPool;

import message.Message;

public class Session implements Runnable {
	
	List<Message> recMsgList = new ArrayList<Message>();
	private static List<Message> sendMessageList = new ArrayList<Message>();
	
	boolean isRunning = false;
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
		isRunning = true;
	}
	
	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		while (isRunning) {
			synchronized (sendMessageList) {
				if (!sendMessageList.isEmpty()) {
					Message msg = sendMessageList.remove(0);

					Player pTarget = PlayerPool.getPlayerByUUid(msg.getTarget());
					pTarget.getSocket().putMessage(msg.getData());
				}
			}
		}
	}
	
	public static void submitMessage(Message submit) {
		synchronized (sendMessageList) {
			sendMessageList.add(submit);
		}
	}
}
