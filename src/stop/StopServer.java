package stop;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class StopServer {
	public static void main(String args[]) {
		try {
			DatagramSocket stopSoc = new DatagramSocket();
			DatagramPacket data = new DatagramPacket(new byte[1], 1);
			data.setSocketAddress(new InetSocketAddress("127.0.0.1", 6001));
			data.setData(new byte[] { -1 });
			stopSoc.send(data);
		}
		catch (Exception e) {
			
		}
	}
}
