import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server{

	private static InfoGame game;
	private static String ipAddress;
	private static String serverAddress = "";
	private static Node serverInfo;
	private static ListAddr listAddr;
	private static ServerChat discussion;
	private static int numPlayer = 1;
	private static Server server = null;
	private static int socketPortClient;

	boolean isInitialized = false;
	private static boolean playing = false;
	private static boolean gameStopped = false;
	private static boolean allConnected = false;

	int numberFinished;
	final int maxghosts = 12;
	int[] dx, dy;
	static int[] ghostdx, ghostdy, ghostspeed;

	//ghostMove serve to know when we have to move ghosts
	//It's used in PlayGame
	int ghostMove = 0;


	private static I_InfoPlayer player = null;

	final short leveldata[] =
		{ 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
			21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
			21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 
			21, 0,  0,  0,  17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 
			17, 18, 18, 18, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 20,
			17, 16, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 16, 24, 20, 
			25, 16, 16, 16, 24, 24, 28, 0,  25, 24, 24, 16, 20, 0,  21, 
			1,  17, 16, 20, 0,  0,  0,  0,  0,  0,  0,  17, 20, 0,  21,
			1,  17, 16, 16, 18, 18, 22, 0,  19, 18, 18, 16, 20, 0,  21,
			1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21, 
			1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21,
			1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0,  21,
			1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  21,
			1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
			9,  8,  8,  8,  8,  8,  8,  8,  8,  8,  25, 24, 24, 24, 28 };


	final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
	final int maxspeed = 6;
	int currentspeed = 3;



	public Server() {

			game.setServer(this);
			game.InitScreenData();
			game.Initghostx(new int[maxghosts]);
			ghostdx = new int[maxghosts];
			game.Initghosty(new int[maxghosts]);
			ghostdy = new int[maxghosts];
			ghostspeed = new int[maxghosts];
			dx = new int[4];
			dy = new int[4];

			numberFinished = 0;
	}


	private static void serverWaiting() throws Exception{
		System.out.println("Waiting Mode");
		ServerSocket waitingSocket = new ServerSocket(serverInfo.getPort());
		String line;

		while(true)
         	{
            		Socket connectionSocket = waitingSocket.accept();
            		BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            		DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
			//System.out.println("Waiting for migration");
            		line = in.readLine();
            		//System.out.println("Received: " + line);
			String info = line;
			out.writeBytes("Info OK\n");
			line = in.readLine();
            		//System.out.println("Received: " + line);
			serverInit();
			game.setMoving(true);
			reconstruction(info);
			out.writeBytes("Bind OK\n");
			waitingSocket.close();
			break;
         	}
		while(!allConnected){
            		System.out.print("");
		}
		game.setMoving(false);
            		//System.out.println("Fin server waiting");
			serverRunning();
	}

	public static void reconstruction(String s) {
		try {
			String[] infos = s.split("%%%%%");

			game.construction(infos[0]);
			serverConstruction(infos[1]);
		} catch (RemoteException e){
			System.out.println("Failed during reconstruction of the game");
		}
	}

	public static void serverConstruction(String s){
		String[] infos = s.split(" ");
			int cont = 0;
		for (int i = 0; i < game.getNrofghosts(); i++) {
			ghostdx[i] = Integer.valueOf(infos[cont]);
			cont++;
		}
		for (int i = 0; i < game.getNrofghosts(); i++) {
			ghostdy[i] = Integer.valueOf(infos[cont]);
			cont++;
		}
		for (int i = 0; i < game.getNrofghosts(); i++) {
			ghostspeed[i] = Integer.valueOf(infos[cont]);
			cont++;
		}
	}
		

	public static void serverInit() {
		String serverAddress = "rmi://"+serverInfo.getIpAddr()+":1099/I_InfoGame";
		try{
			game = new InfoGame();
			server = new Server();
			game.setServer(server);
			game.setServerAddress(serverInfo.getIpAddr());
			game.setNextAddress(serverInfo.getNext().getIpAddr());
			game.setNbPlayerExpected(numPlayer);
			Naming.rebind(serverAddress, game);
			socketPortClient = serverInfo.getPortClient();
			//System.out.println("End initialisation");
		} catch (RemoteException e){
			System.out.println("Hubo una excepcion creando la instancia del objeto distribuido");
		} catch (MalformedURLException e){
			System.out.println("URL mal formada al tratar de publicar el objeto");
		}
	}

	public static void setDiscussion() {
		discussion = new ServerChat(socketPortClient);
		Thread thread = new Thread(discussion);
		thread.start();
		try {
			game.setUnbind(true);
		} catch (RemoteException e) {
			System.out.println("Failed setUnbind on game");
		}
	}

		public int getClientPort(){
			return socketPortClient;
		}

	public static void sendInfo(DataOutputStream out) {
		String info = infoComplete()+"\n";
		try {
			out.writeBytes(info);
		} catch(Exception e) {
			System.out.println("Failed to send info");
		}
		//System.out.println("Info sent");
	}

	private static void serverCall() throws Exception{
		String sentence = "move";
		String line;

		while(!gameStopped){
			System.out.print("");
		}
		playing = false;
		try {
			setDiscussion();
		}
		catch (Exception ex){
			System.out.println("Failed to discuss with clients");
		}

  		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		try {
			InetAddress inetAddress = InetAddress.getByName(serverInfo.getNext().getIpAddr());
  			Socket clientSocket = new Socket(inetAddress,serverInfo.getNext().getPort());
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
  			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sendInfo(out);
			line = in.readLine();
			while(!game.getUnbinding());
			Naming.unbind("rmi://"+serverInfo.getIpAddr()+":1099/I_InfoGame");
			out.writeBytes("Unbinding finished\n");
			line = in.readLine();
  			clientSocket.close();
		} catch (RemoteException e){
			System.out.println("Failed get info from game");
			System.exit(1);
		} catch (Exception ex){
			System.out.println("Failed to socket with waiting server");
		}
		discussion.shutDown();
		discussion = null;
		game = null;
		System.gc();
		serverWaiting();	
	}

	public void setPlaying(boolean b) {
		playing = b;
	}

	public void setGameStopped(boolean b) {
		gameStopped = b;
	}

	public void setAllConnected(boolean b) {
		allConnected = b;
	}

	private static void serverRunning() {
		System.out.println("Server Running");
		ServerTimer timer = new ServerTimer(60);
		Thread thread = new Thread(timer);
		thread.start();
		//System.out.println("Waiting for timer");
		while(!timer.finished());
		timer.shutDown();
		//System.out.println("Migration !");
		timer = null;
		System.gc();
		try {
			game.setMoving(true);
			serverCall();
		}catch (RemoteException e){
			System.out.println("Failed to modify game");
			System.exit(1);
		}catch (Exception ex){
			System.out.println("Failed to call");
		}

		
	}


	public void moveGhosts() {
		try {
			int nbrPlayerInGame = game.getNbrPlayerInGame();
			int[] ghosty = game.getGhosty();
			int[] ghostx = game.getGhostx();
			if (ghostMove==0){
				short i;
				int pos;
				int count;
				int blocksize = game.getBlocksize();
				int nrofblocks = game.getNrofblocks();
				int nrofghosts = game.getNrofghosts();
				short[] screendata = game.getScreendata();



				for (i = 0; i < nrofghosts; i++) {
					if (ghostx[i] % blocksize == 0 && ghosty[i] % blocksize == 0) {
						pos = ghostx[i] / blocksize + nrofblocks * (int)(ghosty[i] / blocksize);

						count = 0;
						if ((screendata[pos] & 1) == 0 && ghostdx[i] != 1) {
							dx[count] = -1;
							dy[count] = 0;
							count++;
						}
						if ((screendata[pos] & 2) == 0 && ghostdy[i] != 1) {
							dx[count] = 0;
							dy[count] = -1;
							count++;
						}
						if ((screendata[pos] & 4) == 0 && ghostdx[i] != -1) {
							dx[count] = 1;
							dy[count] = 0;
							count++;
						}
						if ((screendata[pos] & 8) == 0 && ghostdy[i] != -1) {
							dx[count] = 0;
							dy[count] = 1;
							count++;
						}

						if (count == 0) {
							if ((screendata[pos] & 15) == 15) {
								ghostdx[i] = 0;
								ghostdy[i] = 0;
							} else {
								ghostdx[i] = -ghostdx[i];
								ghostdy[i] = -ghostdy[i];
							}
						} else {
							count = (int)(Math.random() * count);
							if (count > 3)
								count = 3;
							ghostdx[i] = dx[count];
							ghostdy[i] = dy[count];
						}

					}
					ghostx[i] = ghostx[i] + (ghostdx[i] * ghostspeed[i]);
					ghosty[i] = ghosty[i] + (ghostdy[i] * ghostspeed[i]);


				}
			}
			ghostMove =(ghostMove + 1) % nbrPlayerInGame;
			game.setGhostx(ghostx);
			game.setGhosty(ghosty);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void GameInit() {
		if (!isInitialized){
			LevelInit();
			try {
				game.setNrofghosts(6);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentspeed = 3;
		}
	}


	public void LevelInit() {
		try{
			for(int i = 0; i<game.getNrofblocks()*game.getNrofblocks(); i++)
				game.setScreendata(leveldata[i],i);
			LevelContinue();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void LevelContinue() {
		short i;
		int dx = 1;
		int random;

		try {
			int blocksize = game.getBlocksize();
			int[] ghosty = game.getGhosty();
			int[] ghostx = game.getGhostx();
			int nrofghosts = game.getNrofghosts();

			for (i = 0; i < nrofghosts; i++) {
				ghosty[i] = 4 * blocksize;
				ghostx[i] = 4 * blocksize;
				ghostdy[i] = 0;
				ghostdx[i] = dx;
				dx = -dx;
				random = (int)(Math.random() * (currentspeed + 1));
				if (random > currentspeed)
					random = currentspeed;
				ghostspeed[i] = validspeeds[random];
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void finished() {
		int numberPlayer = game.getNumberPlayer();
		numberFinished += 1;
		if(numberFinished == numberPlayer){
			//Ghosts update
			int nrOfGhosts = game.getNrofghosts();
				if (nrOfGhosts < maxghosts){
					game.setNrofghosts(nrOfGhosts++);
				}
				if (currentspeed < maxspeed){
					currentspeed++;
				}
				LevelInit();
		}
	}

	/*
	 *function who transformed all the caracteristic of InfoGame in a string
	 *to sent them at the new server
	 */
	public static String infoGame(){
		String info = "";
		try{
		info = Integer.toString(game.getNrofghosts());
		info += " " + Integer.toString(game.getDeathcounter());
		int[] ghostx = game.getGhostx();
		for (int i = 0; i < game.getNrofghosts(); i++) {
			info += " " + Integer.toString(ghostx[i]);
		}
		int[] ghosty = game.getGhosty();
		for (int i = 0; i < game.getNrofghosts(); i++) {
			info += " " + Integer.toString(ghosty[i]);
		}
		short[] screenData = game.getScreendata();
		for (int i = 0; i < screenData.length; i++) {
			info += " " + Integer.toString(screenData[i]);
		}
		info += " " + Integer.toString(game.getNumberPlayer());
		info += " " + Integer.toString(game.getNbPlayerExpected());
		info += " " + Integer.toString(game.getNbPlayerWaiting());
		info += " " + Integer.toString(game.getNbrPlayerInGame());
		info += " " + String.valueOf(game.isPlaying());
		info += " " + String.valueOf(game.isWaiting());
		info += " " + String.valueOf(game.isEnded());
		info += " " + String.valueOf(game.isPause());
		info += " " + game.getPlayerCallPause();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return info;
	}
	
	/*
	 *function who transformed all the caracteristic of player in a string
	 *to sent them at the new server
	 */
	public static String infoPlayer(I_InfoPlayer player){
		String info = "";
		try{
		info +=  Integer.toString(player.getPacsleft());
		info += " " + Integer.toString(player.getScore());
		info += " " + Integer.toString(player.getPacmanx());
		info += " " + Integer.toString(player.getPacmany());
		info += " " + Integer.toString(player.getPacmandx());
		info += " " + Integer.toString(player.getPacmandy());
		info += " " + Integer.toString(player.getViewdx());
		info += " " + Integer.toString(player.getViewdy());
		info += " " + Integer.toString(player.getReqdx());
		info += " " + Integer.toString(player.getReqdy());
		info += " " + String.valueOf(player.getDying());
		info += " " + String.valueOf(player.isPlaying());
		info += " " + String.valueOf(player.isWaiting());
		info += " " + String.valueOf(player.isDead());
		info += " " + player.getName();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return info;
	}

	public static String infoComplete(){
		String info = "";
		try{
			//Board
			info = infoGame();

			//Players
			ArrayList<I_InfoPlayer> players = game.getPlayers();
			for (int i = 0; i<players.size(); i++){
				info += "%%%" + infoPlayer(players.get(i));
			}
			info+="%%%%%";
				info +=Integer.toString(ghostdx[0]);
			for (int i = 1; i < game.getNrofghosts(); i++) {
				info += " " + Integer.toString(ghostdx[i]);
			}
			for (int i = 0; i < game.getNrofghosts(); i++) {
				info += " " + Integer.toString(ghostdy[i]);
			}
			for (int i = 0; i < game.getNrofghosts(); i++) {
				info += " " + Integer.toString(ghostspeed[i]);
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return info;
	}
    
    public void disconnect(String name, I_InfoPlayer player) {
        try {
            Naming.unbind("rmi://"+serverInfo.getIpAddr()+":1099/I_InfoGame");
            Naming.unbind("rmi://"+serverInfo.getIpAddr()+":1099/"+name);
        } catch (Exception e){
            System.out.println("Failed unbind");
            System.exit(128);
        }
        System.out.println("Disconnected");
        ServerTimer timer = new ServerTimer(5);
		Thread thread = new Thread(timer);
		thread.start();
		while(!timer.finished());
		timer.shutDown();
		timer = null;
		System.gc();
        try {
            Naming.rebind("rmi://"+serverAddress+":1099/I_InfoGame", game);
            Naming.rebind("rmi://"+serverAddress+":1099/"+name, player);
        } catch (Exception e){
            System.out.println("Failed rebind");
            System.exit(128);
        }
        System.out.println("Reconnected");
    }


	public static void main(String[] args){
		// Establecimiento del game en el rmiserver
		if (args.length == 0) {
			System.out.println("Wrong number of argument");
			System.exit(1);
		}

		listAddr = new ListAddr();
		serverInfo = listAddr.getServer(args[args.length-1]);
		
		
		if (args.length == 2 && args[0].equals("wait")){
			try {
				serverWaiting();
			}
			catch (Exception e) {
				System.out.println("Failed to wait");
			}
		}
		else {
			if (args.length == 2) numPlayer = Integer.parseInt(args[0]);
			//System.out.println("Initialisation");
			serverInit();
			serverRunning();
		}

		

		
	}
}


