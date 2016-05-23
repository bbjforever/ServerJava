package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import message.Message;

import script.Script;

public class Client {
	public Client() {
		try {
	        Socket s=new Socket(InetAddress.getByName(null), 6000);//"localhost" "127.0.0.1"
	        OutputStream os = s.getOutputStream();
	        InputStream is = s.getInputStream();
	        
	        Script send = new Script();
	        send.putInt(-1);
	        
	        os.write(send.toByteArray());
	        Thread.sleep(100);
	        
	        os.close();
	        is.close();
	        s.close();
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		finally {
			System.err.println("Exception : client finally!");
		}
	}
}