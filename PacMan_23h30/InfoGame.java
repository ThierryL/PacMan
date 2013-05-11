

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

    private ArrayList<I_InfoPlayer> players;

	private short[] screendata;

	private Server server = null;
    	private boolean begin = false;
	private int numberPlayer = 0;
	private int playerWaiting = 0;
	private int playerInGame = 0;

	private static final long serialVersionUID = 1L;

	protected InfoGame() throws RemoteException {
		super();
	}
	
	public void setNumberPlayer(int n) {
		numberPlayer = n;
	}

	public int getNumberPlayer() {
		return numberPlayer;
	}

    public void newPlayer(String name) throws RemoteException{

	I_InfoPlayer player = null;
	try {
		player = (I_InfoPlayer) Naming.lookup("rmi://localhost:1099/"+name);
		players.add(player);
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

    public ArrayList<I_InfoPlayer> getPlayers() {
	    return players;
    }

    public void setServer(Server s) {
    	server = s;
	players = new ArrayList<I_InfoPlayer>();
    }
    
    public boolean isBegin() {
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
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
		for(int i = 0; i<players.size(); i++)
			server.GameInit(players.get(i));
	}

	public void PlayGame(I_InfoPlayer p) {
		server.PlayGame(p,players);
	}

	public boolean checkInit() {
		if (playerWaiting<numberPlayer) return false;
		playerInGame = playerWaiting;
		return true;
	}

	public void addPlayer() {
		playerWaiting++;
	}

	public void finished() {
		try{
		I_InfoPlayer tmp = players.get(0);
		for (int i = 0; i<players.size(); i++)
			if (tmp.getScore() < players.get(i).getScore()) tmp = players.get(i);
		for (int i = 0; i<players.size(); i++) {
			if (!tmp.getName().equals(players.get(i).getName())) players.get(i).isLooser();
			else players.get(i).isWinner();
		}
		playerWaiting = 0;
		playerInGame = 0;
		} catch (Exception exc) {
				exc.printStackTrace();
        	}
	}

	public void exit(String name) {
		try {
		for (int i=0; i<players.size(); i++) {
 			String val = players.get(i).getName();
 			if (val.equals(name)) {
   				players.remove(i);
				System.out.println("player in: "+playerInGame+"   player waiting: "+playerWaiting);
				playerWaiting --;
				if (playerInGame != 0) {
					playerInGame--;
					if (playerInGame<numberPlayer) {
						for (int k=0; k<players.size(); k++)
							players.get(k).reset();
						setBegin(false);
						server.reset();
						playerInGame = 0;
						playerWaiting = 0;
					}
				}
				System.out.println("player in: "+playerInGame+"   player waiting: "+playerWaiting);
   				break;
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

}

