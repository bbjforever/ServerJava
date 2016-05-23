package player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayerPool {
	private static LinkedList<Player> m_playerPool = new LinkedList<Player>();
	
	public static void addPlayer(Player p) {
		synchronized (m_playerPool) {
			m_playerPool.addLast(p);
		}
	}
	
	public static Player getPlayerByUUid(String uuid) {
		synchronized (m_playerPool) {
			Iterator<Player> it = m_playerPool.iterator();
			while (it.hasNext()) {
				Player p = it.next();
				if (p.getUUid() == uuid) {
					return p;
				}
			}
			
			return null;
		}
	}
}
