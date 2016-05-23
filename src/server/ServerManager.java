package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import player.Player;
import player.PlayerPool;

import message.Message;
import module.Module;

import script.Script;

public class ServerManager implements Runnable {
	private static Map<String, Server> m_map = new HashMap<String, Server>();
	private static Map<Integer, List<Module>> m_insideNotifyMap = new HashMap<Integer, List<Module>>();
	
	private static ServerSocket m_serverSoc;
	private StopSocket m_stopSoc;

	private boolean m_isRunning = false;
	
	public ServerManager() {
		try {
			m_serverSoc = new ServerSocket(6000, 256);
			System.out.println("ServerSocket 初始化成功！");
		}
		catch (Exception e) {
			System.err.println("ServerSocket 初始化失败！");
		}
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
		
		m_stopSoc = new StopSocket();
		m_stopSoc.start();
	}

	public static void registerServer(Server server, String uuid) {
		synchronized (m_map) {
			m_map.put(uuid, server);
		}
	}
	
	public static void registerInsidNotify(int notifyId, Module mod) {
		List<Module> modList = m_insideNotifyMap.get(notifyId);
		if (modList == null) {
			modList = new ArrayList<Module>();
			m_insideNotifyMap.put(notifyId, modList);
		}
		modList.add(mod);
	}

	public static Server getServer(String uuid) {
		synchronized (m_map) {
			return m_map.get(uuid);
		}
	}
	
	public void stop() {
		m_isRunning = false;
		
		try {
			synchronized (m_map) {
				Set<String> keySet = m_map.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String uuid = it.next();
					stopServer(uuid);
				}
			}
		}
		finally {
			try {
				m_serverSoc.close();
				System.out.println("ServerSocket关闭成功！");
			} catch (Exception e) {
				System.err.println("ServerSocket无法关闭！");
			}
		}
	}
	
	public static void stopServer(Server server) {
		synchronized (m_map) {
			server.stop();
			m_map.remove(server.getId());
			System.out.println("Server Id = " + server.getId() + " 关闭成功！");
		}
	}
	
	public static void stopServer(String uuid) {
		Server server = getServer(uuid);
		if (server != null)
			stopServer(server);
	}
	
	public void run() {
		m_isRunning = true;
		while (m_isRunning) {
			try {
				Socket soc = m_serverSoc.accept();
				Server server = new Server(soc);
				server.start();
			} catch (Exception e) {
				System.err.println("ServerSocket无法生成新的socket！");
			}
		}
	}
	
	/* 监听关闭信息的套接字 */
	private class StopSocket implements Runnable {
		private DatagramSocket m_stopSoc;
		
		public void start() {
			Thread thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			try {
				m_stopSoc = new DatagramSocket(new InetSocketAddress("127.0.0.1", 6001));
				m_stopSoc.receive(new DatagramPacket(new byte[1], 1));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				m_stopSoc.close();
				ServerManager.this.stop();
			}
		}
	}
}
