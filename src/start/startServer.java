package start;

import server.ServerManager;

class startServer {
	public static void main(String args[]) {
		ServerManager manager = new ServerManager();
		manager.start();
	}
}