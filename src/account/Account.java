package account;

import player.Player;
import player.PlayerPool;
import script.Script;
import message.Message;
import module.Module;

public class Account extends Module {
	
	private final int MSG_Login = 2000;

	@Override
	public void messageHandle(Message msg) {
		int type = msg.getType();
		switch (type) {
		case MSG_Login://µÇÂ¼
			doLogin(msg);
			break;
		}
	}
	
	private void doLogin(Message msg) {
		String recUuid = msg.getTarget();
		Player p = PlayerPool.getPlayerByUUid(recUuid);
		
		Script send = new Script();
		send.putString(recUuid);
		
		p.putMessage(new Message(send, MSG_Login));
	}

}
