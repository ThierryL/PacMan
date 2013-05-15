
import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class server{

	private static Iface1 stub;
	private static String ipAddress;
	private static String serverAddress = "";

	private static void serverWaiting() throws Exception{
		ServerSocket waitingSocket = new ServerSocket(9999);
		String clientSentence;

		while(true)
         	{
            		Socket connectionSocket = waitingSocket.accept();
            		BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            		DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Waiting for migration");
            		clientSentence = in.readLine();
            		System.out.println("Received: " + clientSentence);
			waitingSocket.close();
			serverInit();
         	}

	}

	private static void serverCall() throws Exception{
		String sentence = "move";
  		String modifiedSentence;
		System.out.println("Waiting for client to unbind");
		ServerSocket clientInfo = new ServerSocket(9998);
		Thread thr = new ServerThread(clientInfo);
		thr.start();
  		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		try {
			InetAddress inetAddress = InetAddress.getByName(stub.getNextAddress());
  			Socket clientSocket = new Socket(inetAddress,stub.getNextPort());
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
  			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  			out.writeBytes(sentence + '\n');
  			clientSocket.close();
		} catch (RemoteException e){
			System.out.println("Failed get info from stub");
			System.exit(1);
		}
			
	}

	private static void serverInit() {
		String serverAddress = "rmi://"+ipAddress+":1099/Iface1";
		String nextAddress = "127.0.0.1";
		int nextPort = 9999;
		String line;
		try{
			stub = new IfaceImpl1();
			System.out.println(serverAddress);
			Naming.rebind(serverAddress, stub);
			System.out.println("Begin Scanner");
			Scanner in = new Scanner(System.in);
			line = in.nextLine();
			if(line.equals("move")) {
				System.out.println("Moving");
				stub.setMoving(true);
				stub.setNextAddress(nextAddress);
				stub.setNextPort(nextPort);
				try {
					serverCall();
				}
				catch (Exception ex){
					System.out.println("Failed to call");
				}
				System.out.println("Moved");
			}
			else System.out.println("nop");
		} catch (RemoteException e){
			System.out.println("Hubo una excepcion creando la instancia del objeto distribuido");
		} catch (MalformedURLException e){
			System.out.println("URL mal formada al tratar de publicar el objeto");
		}
	}

	public static void main(String[] args){
		// Establecimiento del stub en el rmiserver
		
		if (args.length == 0) {
			System.out.println("Wrong number of argument");
			System.exit(1);
		}
		ipAddress = args[0];
		if (args.length == 2 && args[1].equals("wait")){
			System.out.println("Waiting Mode");
			try {
				serverWaiting();
			}
			catch (Exception e) {
				System.out.println("Failed to wait");
			}
		}
		else {
			System.out.println("Initialisation");
			serverInit();
		}

		

		
	}
}


