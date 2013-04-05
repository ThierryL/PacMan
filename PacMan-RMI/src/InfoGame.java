import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;





public class InfoGame  extends UnicastRemoteObject implements I_InfoGame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected InfoGame() throws RemoteException {
		super();
	}

	private Server server = null;
    private final int blocksize = 24;
    private final int nrofblocks = 15;
    private final int scrsize = nrofblocks * blocksize;
    private int nrofghosts = 6;
    private int deathcounter = 0;
    private int[] ghostx, ghosty;

    private ArrayList<InfoPlayer> players;

	private short[] screendata;

    private boolean begin = false;

    public void newPlayer(){
//	    InfoPlayer player = null;
//	    try {
//	    player= new InfoPlayer();
//	    } catch (Exception e) {
//		    e.printStackTrace();
//	    }
//	    return player;
//
    }

    public void setServer(Server s) {
    	server = s;
    }
    
    public boolean isBegin() {
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
	}
    
    public void InitScreenData(){
        setScreendata(new short[nrofblocks * nrofblocks]);
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


	public void setScreendata(short[] screendata) {
		this.screendata = screendata;
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
		InfoPlayer player = null;
		if (!isBegin() && players == null) {
			players = new ArrayList<InfoPlayer>();

			try {
				player = (InfoPlayer) Naming.lookup("rmi://localhost:1099/InfoPlayer");
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

			players.add(player);
			setBegin(true);
			server.GameInit(player);
		}
		else {
			server.GameInit(players.get(0));
		}
	}

	public void PlayGame() {
		server.PlayGame(players);
	}

	public void notifyMove() {
		PlayGame();
	}

}

