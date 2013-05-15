
import java.io.*;
import java.net.*;

public class HandlerThread extends Thread {
	private Socket s;
	private boolean running = true;

	HandlerThread(Socket s) {
		this.s = s;
	}

	public void shutDown() {
		running = false;
	}	

	public void run() {
		try {
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			while(running) {
				out.writeBytes("ping");
			}
			out.writeBytes("moved");
			s.close();
		} catch (Exception e) {
			System.out.println("Failed to close ServerSocket");
		}
	}
}
