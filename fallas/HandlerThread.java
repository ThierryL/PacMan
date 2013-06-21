import java.io.*;
import java.net.*;

public class HandlerThread extends Thread {
	private Socket s;
	private boolean shutDown = false;
	
	HandlerThread(Socket s) {
		this.s = s;
	}

	public void shutDown() {
		shutDown = true;
	}	

	public void run() {
		String message;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			while (!shutDown){
            			out.writeBytes("ping\n");
			}
			out.writeBytes("moved\n");
			message = in.readLine();
			System.out.println(message);
			s.close();
		} catch (Exception e) {
			System.out.println("Failed to close ServerSocket");
		}
	}
}
