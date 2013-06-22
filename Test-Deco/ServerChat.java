import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerChat implements Runnable{

private ServerSocket clientInfo;
private ArrayList<HandlerThread> listThread = null;
private boolean shutDown = false;
private int port;

public ServerChat(int port)  {
	this.port=port;
	listThread = new ArrayList<HandlerThread>();
	}

	public void run() {
		try{
			clientInfo = new ServerSocket(port);
			while (!shutDown){
				Socket ss = clientInfo.accept();
				HandlerThread newClient = new HandlerThread(ss);
				Thread thread = new Thread(newClient);
				thread.start();
				listThread.add(newClient);
			}
		}  catch (Exception e){
			e.printStackTrace();
			System.out.println("Failed to chat");
		}
		
		try {
			clientInfo.close();
		}  catch (Exception e){
			System.out.println("Failed to close");
		}
	}
	
	public void shutDown() {
		for (int i = 0; i<listThread.size(); i++) ((HandlerThread) listThread.get(i)).shutDown();
		shutDown = true;
	}

}
