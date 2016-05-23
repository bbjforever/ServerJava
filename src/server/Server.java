package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import message.Message;
import module.Module;
import player.Player;
import player.PlayerPool;
import script.Script;

public class Server implements Runnable {
	
	private Socket m_socket;
	private String m_uuid;
	private boolean m_isConnect;
	
	public static Map<Integer, Module> m_map = new HashMap<Integer, Module>();
	
	private InputStream m_is;
	private OutputStream m_os;
	
	public Server(Socket socket) {
		this.m_socket = socket;
	}
	
	public void start() {
		try {
			System.out.println("有客户端接入");
			
        	m_is = m_socket.getInputStream();
        	m_os = m_socket.getOutputStream();
        	m_isConnect = true;
        	
			Thread thread = new Thread(this);
			thread.start();
		}
		catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("Server id = " + m_uuid + " start() exception!");
		}
	}

	@Override
	public void run() {
		Player newPlayer = new Player(this);
		PlayerPool.addPlayer(newPlayer);
		
		m_uuid = newPlayer.getUUid();
		ServerManager.registerServer(this, m_uuid);

	    process();
	}
	
	private void process() {
		while (m_isConnect) {
			try {
	        	/* Parse client data */
		        byte[] buf = new byte[1024];
		        m_is.read(buf);
		        
		        Script rec = new Script(buf);
		        String ss = rec.getString();
		        int ii = rec.getInteger();
		        
		        int type = rec.getInteger();
		        Module mod = m_map.get(type);
		        if (mod != null) {
			        Message msg = new Message(rec, type);
		        	mod.messageHandle(msg);
		        }
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
			catch (Exception e) {
				System.err.println("Server id = " + m_uuid + " process() exception!");
			}
		}
	}
	
	public void putMessage(Script send) {
		try {
			m_os.write(send.toByteArray());
		}
		catch (Exception e) {
			System.err.println("Server id = " + m_uuid + " putMessage() exception!");
		}
		finally {
			
		}
	}
	
	public void stop() {
	    /* clean */
		try {
	        m_os.close();
	        m_is.close();
	        m_socket.close();

			m_isConnect = false;
			System.out.println("Server id = " + m_uuid + " stop() Succeed!");
		}
		catch (Exception e) {
			System.err.println("Server id = " + m_uuid + " stop() exception!");
		}
		finally {
		}
	}

	public String getId() {
		return m_uuid;
	}
}