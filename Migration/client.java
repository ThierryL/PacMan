import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.io.*;
import java.net.*;

import java.util.Arrays;

public class client{

	private static Iface1 skeleton = null;
	private static String currentAddress = "";
	private static String nextAddress = "";

	private static void waitingForNewServer() {
		String info;
		try {
		InetAddress inetAddress = InetAddress.getByName(currentAddress);
  		Socket clientSocket = new Socket(inetAddress, 9998);
  		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
  		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  		out.writeBytes("Checking\n");
  		info = in.readLine();
  		System.out.println("Server Moved: "+info);
  		clientSocket.close();
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("Failed socket to server");
		}
	}

	private static void changeServer() {
		try{
			nextAddress = skeleton.getNextAddress();
		} catch (RemoteException e){
			System.out.println("Remote error");
		}

		try {  
			skeleton.unbinding();
			Naming.unbind("rmi://"+currentAddress+":1099/Iface1");  
		}  catch (Exception e) {  
			System.out.println("Failed To unbind object with the RMI registry");  
		}

		try{
			waitingForNewServer();
			skeleton = (Iface1) Naming.lookup("rmi://"+nextAddress+":1099/Iface1");
			currentAddress = nextAddress;
			
		} catch (NotBoundException e){
			System.out.println("El servicio no esta publicado en el servidor");
			System.exit(128);
		} catch (MalformedURLException e){
			System.out.println("URL invalida");
			System.exit(128);
		} catch (RemoteException e){
			System.out.println("Excepcion remota tratanod de conectarse al servidor");
			System.exit(128);
		}
	}

	private static void play() {
		try{
			System.out.println("Playing");
			while(true) {
				if(skeleton.moving()) {
					System.out.println("Moving");
					changeServer();
					break;
				}
			}
		} catch (RemoteException e){
			System.out.println("Remote error");
		}
	}

	public static void main(String[] args){
		int[] a, b, c;

		if (args.length != 1) {
			System.out.println("Wrong number of argument");
			System.exit(1);
		}
		currentAddress = args[0];
		try{
			skeleton = (Iface1) Naming.lookup("rmi://"+currentAddress+":1099/Iface1");
		} catch (NotBoundException e){
			System.out.println("El servicio no esta publicado en el servidor");
			System.exit(128);
		} catch (MalformedURLException e){
			System.out.println("URL invalida");
			System.exit(128);
		} catch (RemoteException e){
			System.out.println("Excepcion remota tratanod de conectarse al servidor");
			System.exit(128);
		}


		while(true) {
			play();
		}

	}
}
