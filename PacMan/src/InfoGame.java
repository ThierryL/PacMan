import java.awt.Graphics2D;
import java.util.ArrayList;





public class InfoGame {

	private Server server = null;
    private final int blocksize = 24;
    private final int nrofblocks = 15;
    private final int scrsize = nrofblocks * blocksize;
    private int nrofghosts = 6;
    private int deathcounter;
    private int[] ghostx, ghosty;

    private ArrayList<InfoPlayer> players;

	private short[] screendata;

    private boolean begin = false;

    
    public InfoPlayer createInfoPlayer() {
    	return new InfoPlayer();
    }
    
    public void setServer(Server s) {
    	server = s;
    }
    
    public boolean isBegin() {
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
		System.out.println("begin");
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

	public void GameInit(InfoPlayer player) {
		if (!isBegin() && players == null) {
			players = new ArrayList<InfoPlayer>();
			players.add(player);
			setBegin(true);
			server.GameInit(player);
		}
		else {
			server.GameInit(player);
		}
	}

	public void PlayGame() {
		server.PlayGame(players);
	}

	public void notifyMove() {
		PlayGame();
	}

}

