
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
	private ServerSocket s;
	private ArrayList listThread = null;
	private boolean shutDown = false;
	
	ServerThread(ServerSocket s) {
		this.s = s;
		this.listThread = new ArrayList();
	}

	public void turnOff() {
		for (int i = 0; i<listThread.size(); i++) ((HandlerThread) listThread.get(i)).shutDown();
		try {
			s.close();
		} catch (Exception e) {
			System.out.println("Failed to close ServerSocket");
		}
	}
	
	public void run() {
		while (!shutDown) {
			try {
				Socket ss = s.accept();
				Thread thr = new HandlerThread(ss);
				thr.start();
				listThread.add(thr);
			}  catch (Exception e){
				System.out.println("Failed to accept connection");
			}
		}
	}
}
