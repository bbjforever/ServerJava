package module.chat;

import player.Player;
import player.PlayerPool;
import script.Script;
import message.Message;
import module.Module;

public class Chat extends Module {
	
	private final static int MSG_Start = 1000;
	private final static int MSG_Conv = MSG_Start + 0;
	private final static int MSG_Receive = MSG_Start + 1;

	@Override
	public void messageHandle(Message msg) {
		int type = msg.getType();
		switch (type) {
		case MSG_Conv:
			doReceiveChat(msg);
			break;
		case MSG_Receive:
			doReceiveChat(msg);
			break;
		}
	}

	@Override
	public void insideMessage(Message msg) {
		
	}

	public void doReceiveChat(Message msg) {
		Script data = msg.getData();
		
		String targetUuid = data.getString();
		Player target = PlayerPool.getPlayerByUUid(targetUuid);
		Message out = new Message(data, MSG_Receive);
		target.putMessage(out, targetUuid);
	}
}
