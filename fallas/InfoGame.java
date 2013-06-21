import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;





public class InfoGame  extends UnicastRemoteObject implements I_InfoGame{

	/**
	 * 
	 */
	
    private final int blocksize = 24;
    private final int nrofblocks = 15;
    private final int scrsize = nrofblocks * blocksize;
    private int nrofghosts = 6;
    private int deathcounter = 0;
    private int[] ghostx, ghosty;

    private ArrayList<I_InfoPlayer> players = null;

	private short[] screendata;

	private Server server = null;

	private int nbPlayer = 0;

	private int nbPlayerExpected = 1;
	private int nbPlayerWaiting = 0;
	private int nbPlayerPlaying = 0;

	private static final long serialVersionUID = 1L;
	
	private boolean playing = false;
	private boolean waiting = true;
	private boolean ended = false;
	private boolean pause = false;
	private String playerCallPause;

	private String newAddress = "";
	private boolean moving = false;
	private boolean changing = false;
	private int unbinding = 0;
	private boolean unbind = false;
	private String serverAddress;
	private int playerStopped = 0;
	private int nbPlayerReconnected = 0;


	protected InfoGame() throws RemoteException {
		super();
	}
	
	public void construction(String info) throws RemoteException{
		String[] infoComplete = info.split("%%%");
		reconstruction(infoComplete[0]);
		for (int i = 1; i < infoComplete.length; i++) {
			createPlayers(infoComplete[i]);
		}
	}

	private void reconstruction(String infoGame) {
		String[] infos = infoGame.split(" ");
		int cont = 0;
		nrofghosts = Integer.valueOf(infos[cont]);
		cont++;
		deathcounter = Integer.valueOf(infos[cont]);
		cont++;
		ghostx =  new int[nrofghosts];
		for (int i = 0; i < nrofghosts; i++) {
			ghostx[i] = Integer.valueOf(infos[cont]);
			cont++;
		}
		ghosty = new int[nrofghosts];
		for (int i = 0; i < nrofghosts; i++) {
			ghosty[i] = Integer.valueOf(infos[cont]);
			cont++;
		}
		screendata = new short[nrofblocks * nrofblocks];
			for (int i = 0; i < screendata.length; i++) {
				screendata[i] = Short.parseShort(infos[cont]);
				cont++;
			}
		nbPlayer = Integer.valueOf(infos[cont]);
			cont++;

		nbPlayerExpected = Integer.valueOf(infos[cont]);
			cont++;
		nbPlayerWaiting = Integer.valueOf(infos[cont]);
			cont++;
		nbPlayerPlaying = Integer.valueOf(infos[cont]);
			cont++;


		playing = Boolean.valueOf(infos[cont]);
			cont++;
		waiting = Boolean.valueOf(infos[cont]);
			cont++;
		ended = Boolean.valueOf(infos[cont]);
			cont++;
		pause = Boolean.valueOf(infos[cont]);
			cont++;
		playerCallPause = infos[cont];
			cont++;

	}

	private void createPlayers(String infoPlayer) {
		
		String[] infos = infoPlayer.split(" ");

		String namePlayer = infos[14];
		I_InfoPlayer player = null;
		try {
			player = new InfoPlayer(namePlayer);
			players.add(player);

			int cont = 0;
			player.setPacsleft(Integer.valueOf(infos[cont]));
			cont++;
			player.setScore(Integer.valueOf(infos[cont]));
			cont++;
			player.setPacmanx(Integer.valueOf(infos[cont]));
			cont++;
			player.setPacmany(Integer.valueOf(infos[cont]));
			cont++;
			player.setPacmandx(Integer.valueOf(infos[cont]));
			cont++;
			player.setPacmandy(Integer.valueOf(infos[cont]));
			cont++;
			player.setViewdx(Integer.valueOf(infos[cont]));
			cont++;
			player.setViewdy(Integer.valueOf(infos[cont]));
			cont++;
			player.setReqdx(Integer.valueOf(infos[cont]));
			cont++;
			player.setReqdy(Integer.valueOf(infos[cont]));
			cont++;
			player.setDying(Boolean.valueOf(infos[cont]));
			cont++;
			player.setPlaying(Boolean.valueOf(infos[cont]));
			cont++;
			player.setWaiting(Boolean.valueOf(infos[cont]));
			cont++;
			player.setDead(Boolean.valueOf(infos[cont]));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		try {
			Naming.rebind("rmi://"+serverAddress+":1099/"+namePlayer, player);
		} catch (RemoteException e){
			System.out.println("Hubo una excepcion creando la instancia del objeto distribuido");
		} catch (MalformedURLException e){
			System.out.println("URL mal formada al tratar de publicar el objeto");
		}

	}

	public void setServerAddress(String ip) throws RemoteException {
		serverAddress = ip;
	}

	public void unbinding(String s) throws RemoteException{
		unbinding += 1;
		try {
			Naming.unbind("rmi://"+serverAddress+":1099/"+s);
		} catch (Exception e){
			System.out.println("Failed To unbind object with the RMI registry");
		}
	}

	public boolean getUnbinding() throws RemoteException{
		return (unbinding == nbPlayer);
	}

	public boolean gameStopped() throws RemoteException{
		return (playerStopped == nbPlayer);
	}

	public void stopped() throws RemoteException{
		playerStopped++;
		if (playerStopped == nbPlayer){
			server.setGameStopped(true);
		}

	}

	public boolean moving() throws RemoteException{
		return moving;
	}

	public boolean unbind() throws RemoteException{
		return unbind;
	}

	public void setUnbind(boolean b) throws RemoteException{
		unbind = b;
	}

	public void setMoving(boolean b) throws RemoteException{
		moving = b;
	}

	public void playerConnected() throws RemoteException{
		nbPlayerReconnected++;
		if (nbPlayerReconnected == nbPlayer){
			server.setAllConnected(true);
		}
	}

	public boolean allConnected() throws RemoteException{
		return (nbPlayerReconnected == nbPlayer);
	}

	public void setNextAddress(String ad) throws RemoteException{
		newAddress = ad;
	}

	public String getNextAddress() throws RemoteException{
		return newAddress;
	}

    public void newPlayer(String name) throws RemoteException{

	try {

		I_InfoPlayer player;
		//I_InfoPlayer player = findPlayer(name);
		//if (player==null){
			player = new InfoPlayer(name);
			Naming.rebind("rmi://"+serverAddress+":1099/"+name, player);
			players.add(player);
			nbPlayer += 1;
		//}
	} catch (RemoteException e){
		System.out.println("Hubo una excepcion creando la instancia del objeto distribuido");
	} catch (MalformedURLException e){
		System.out.println("URL mal formada al tratar de publicar el objeto");
	}

    }

    public I_InfoPlayer findPlayer(String name){
	    I_InfoPlayer p=null;
	    try {
		    for (int i = 0; i<players.size(); i++){
			    if(players.get(i).getName().equals(name)) {
				    p=players.get(i);
				    break;
			    }
		    }
	    } catch (Exception exc) {
		    exc.printStackTrace();
	    }
	    return p;

    }

    public void moveGhosts() throws RemoteException {
	    server.moveGhosts();
    }

    public void levelContinue() throws RemoteException {
	    server.LevelContinue();
    }

	public  ArrayList<I_InfoPlayer> getPlayers() throws RemoteException {
		return this.players;
	}

//	public void setCurrentSpeed(int currentSpeed) throws RemoteException {
//		this.currentSpeed = currentSpeed;
//	}
//
//	public int getCurrentSpeed() throws RemoteException {
//		return this.currentSpeed;
//	}
//
//	public int getMaxSpeed() throws RemoteException {
//		return this.maxSpeed;
//	}
//
//	public int[] getValidSpeeds() throws RemoteException {
//		return this.validSpeeds;
//	}


	public void setNbPlayerExpected(int n){
		nbPlayerExpected = n;
	}

	public int getNbPlayerExpected() throws RemoteException{
		return nbPlayerExpected;
	}

	public void setNumberPlayer(int n) {
		nbPlayer = n;
	}

	public int getNumberPlayer() {
		return nbPlayer;
	}

	public int getNbrPlayerInGame() throws RemoteException{
		return nbPlayerPlaying;
	}


    public void setServer(Server s) {
    	server = s;
	players = new ArrayList<I_InfoPlayer>();
    }

    public boolean isEnded()  throws RemoteException{
		return ended;
	}

	public void setEnded(boolean ended)  throws RemoteException{
		this.ended = ended;
	}

    
    public boolean isPlaying()  throws RemoteException{
		return playing;
	}

	public void setPlaying(boolean playing)  throws RemoteException{
		this.playing = playing;
	}

    public boolean isPause()  throws RemoteException{
		return pause;
	}

	public void setPause(boolean pause, String player)  throws RemoteException{
		if (!this.pause){
			this.pause = pause;
			this.playerCallPause = player;
		}else if (player.equals(playerCallPause)){
			this.pause=pause;
		}
			
	}

	public String getPlayerCallPause() throws RemoteException{
		return playerCallPause;
	}
	
    public boolean isWaiting()  throws RemoteException{
		return waiting;
	}
	public void setWaiting(boolean waiting)  throws RemoteException{
		this.waiting = waiting;
	}
    
    public void InitScreenData(){
        screendata = new short[nrofblocks * nrofblocks];
    }    
    
    public void Initghostx(int[] m){
        setGhostx(m);
    }   
    
    public void Initghosty(int[] m){
        setGhosty(m);
    }

	public int getNrofghosts() {
		return nrofghosts;
	}


	public void setNrofghosts(int nrofghosts) {
		this.nrofghosts = nrofghosts;
	}


	public int getDeathcounter() {
		return deathcounter;
	}


	public void setDeathcounter(int deathcounter) {
		this.deathcounter = deathcounter;
	}


	public int[] getGhostx() {
		return ghostx;
	}


	public void setGhostx(int[] ghostx) {
		this.ghostx = ghostx;
	}


	public int[] getGhosty() {
		return ghosty;
	}


	public void setGhosty(int[] ghosty) {
		this.ghosty = ghosty;
	}


	public short[] getScreendata() {
		return screendata;
	}


	public void setScreendata(short screendata, int i) {
		this.screendata[i] = screendata;
	}


	public int getBlocksize() {
		return blocksize;
	}


	public int getNrofblocks() {
		return nrofblocks;
	}


	public int getScrsize() {
		return scrsize;
	}

	public void GameInit() {
		server.GameInit();
	}


	public boolean checkInit() {
		return true;

	}


	public void addPlayerWaiting()  throws RemoteException{
		nbPlayerWaiting++;
		if(nbPlayerWaiting == nbPlayerExpected){
			waiting = false;
			playing = true;
			server.setPlaying(true);
			nbPlayerPlaying = nbPlayerWaiting;
			nbPlayerWaiting = 0;
			for (int i = 0; i<players.size(); i++){
				if(players.get(i).isWaiting()){
					players.get(i).setWaiting(false);
					players.get(i).setPlaying(true);
				}
			}

		}
	}

	public int getNbPlayerWaiting() throws RemoteException{
		return nbPlayerWaiting;
	}

	public void addPlayerPlaying()  throws RemoteException{
		nbPlayerPlaying++;
	}

	public void removePlayerPlaying()  throws RemoteException{
		nbPlayerPlaying--;
	}

	public void finished() {
		server.finished();
		try{
	//	I_InfoPlayer tmp = players.get(0);
	//	for (int i = 0; i<players.size(); i++)
	//		if (tmp.getScore() < players.get(i).getScore()) {
	//			tmp = players.get(i);
	//		}
	//	for (int i = 0; i<players.size(); i++) {
	//		if (!tmp.getName().equals(players.get(i).getName())){
	//			players.get(i).isLooser();
	//	}
	//		else players.get(i).isWinner();
	//	}
		//playerWaiting = 0;
		//playerInGame = 0;
		} catch (Exception exc) {
				exc.printStackTrace();
        	}
	}

	public void exit(String name) {
		try {
			int i;
			for (i=0; i<players.size(); i++) {
				String val = players.get(i).getName();
				if (val.equals(name)) {
					break;
				}
			}

			if(players.get(i).isPlaying()){
				nbPlayerPlaying--;
			}
			if(players.get(i).isWaiting()){
				nbPlayerWaiting--;
			}
			nbPlayer--;

			players.remove(i);

			if(playing && (nbPlayerPlaying<nbPlayerExpected)){
				nbPlayerWaiting = nbPlayerPlaying;
				nbPlayerPlaying = 0;
				playing = false;
				waiting = true;
				for (int j = 0; j<players.size(); j++){
					if(players.get(j).isPlaying()){
						players.get(j).setWaiting(true);
						players.get(j).setPlaying(false);
					}
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		try {  
			Naming.unbind("rmi://localhost:1099/"+name);  
		}  catch (Exception e) {  
			System.out.println("Failed To unbind object with the RMI registry");  
		} 
	}

	public boolean allDead() throws RemoteException{

      		for(int k = 0; k<players.size(); k++){
			if(players.get(k).isPlaying()){
				return false;
			}
      		}
		return true;
	}

	public void restart() throws RemoteException{

      		for(int k = 0; k<players.size(); k++){
			players.get(k).playerInit();
      		}
		server.LevelInit();
		waiting = true;
		playing = false;
		ended = false;
	}

	public int getClientPort() throws RemoteException{
		return server.getClientPort();
	}
}

