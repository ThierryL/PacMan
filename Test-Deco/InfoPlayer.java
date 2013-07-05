import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class InfoPlayer extends UnicastRemoteObject implements I_InfoPlayer{



	private static final long serialVersionUID = 1L;
	private final int pacmanspeed = 6;
	private int pacsleft, score;
	private int pacmanx, pacmany, pacmandx, pacmandy;

	private int viewdx, viewdy, reqdx, reqdy;

	private final int blocksize = 24;

	private boolean dying = false;
	boolean playing = false;
	boolean waiting = false;
	boolean dead = false;

	private String name="";


	public InfoPlayer(String name) throws RemoteException{
		super();
		this.name = name;
	}

	/*********************************************************
	 *get and set for Name
	 *********************************************************/
	public void setName(String s) {
		name = s;
	}

	public String getName() {
		return name;
	}

	/*********************************************************
	 *get and set for dead
	 *********************************************************/
	public boolean isDead() throws RemoteException{
		return dead;
	}

	public void setDead(boolean b) throws RemoteException{
		dead = b;
	}

	/*********************************************************
	 *get and set for playing
	 *********************************************************/
	public boolean isPlaying() throws RemoteException{
		return playing;
	}

	public void setPlaying(boolean b) throws RemoteException{
		playing = b;
	}

	/*********************************************************
	 *get and set for waiting
	 *********************************************************/
	public boolean isWaiting() throws RemoteException{
		return waiting;
	}

	public void setWaiting(boolean b) throws RemoteException{
		waiting = b;
	}

	/*********************************************************
	 *get and set for dying
	 *********************************************************/
	public boolean getDying() throws RemoteException{
		return dying;
	}

	public void setDying(boolean b) throws RemoteException{
		dying = b;
	}


	/*********************************************************
	 *get and set for score
	 *********************************************************/
	public int getScore() throws RemoteException{
		return score;
	}

	public void setScore(int i) throws RemoteException{
		score = i;
	}    

	/*********************************************************
	 *get and set for viewdx
	 *********************************************************/
	public int getViewdx() throws RemoteException{
		return viewdx;
	}

	public void setViewdx(int i) throws RemoteException{
		viewdx = i;
	}

	/*********************************************************
	 *get and set for viewdy
	 *********************************************************/
	public int getViewdy() throws RemoteException{
		return viewdy;
	}

	public void setViewdy(int i) throws RemoteException{
		viewdy = i;
	}

	/*********************************************************
	 *get and set for pacmanx
	 *********************************************************/
	public int getPacmanx()  throws RemoteException{
		return pacmanx;
	}

	public void setPacmanx(int pacmanx)  throws RemoteException{
		this.pacmanx = pacmanx;
	}

	/*********************************************************
	 *get and set for pacmany
	 *********************************************************/
	public int getPacmany()  throws RemoteException{
		return pacmany;
	}

	public void setPacmany(int pacmany)  throws RemoteException{
		this.pacmany = pacmany;
	}

	/*********************************************************
	 *get and set for pacmandx
	 *********************************************************/
	public int getPacmandx()  throws RemoteException{
		return pacmandx;
	}

	public void setPacmandx(int pacmandx)  throws RemoteException{
		this.pacmandx = pacmandx;
	}

	/*********************************************************
	 *get and set for pacmandy
	 *********************************************************/
	public int getPacmandy()  throws RemoteException{
		return pacmandy;
	}

	public void setPacmandy(int pacmandy)  throws RemoteException{
		this.pacmandy = pacmandy;
	}

	/*********************************************************
	 *get and set for pacsleft
	 *********************************************************/
	public int getPacsleft()  throws RemoteException{
		return pacsleft;
	}

	public void setPacsleft(int pacsleft)  throws RemoteException{
		this.pacsleft = pacsleft;
	}

	/*********************************************************
	 *get and set for reqdx
	 *********************************************************/
	public int getReqdx()  throws RemoteException{
		return reqdx;
	}

	public void setReqdx(int reqdx)  throws RemoteException{
		this.reqdx = reqdx;
	}

	/*********************************************************
	 *get and set for reqdy
	 *********************************************************/
	public int getReqdy()  throws RemoteException{
		return reqdy;
	}

	public void setReqdy(int reqdy)  throws RemoteException{
		this.reqdy = reqdy;
	}

	/*********************************************************
	 *get and set for pacmanspeed
	 *********************************************************/
	public int getPacmanspeed()  throws RemoteException{
		return pacmanspeed;
	}

	public void playerInit()  throws RemoteException{
		pacsleft = 3;
		score = 0;
		playerInitPos();
		dying = false;
		waiting = false;
		playing = false;
		dead = false;
	}

	public void playerInitPos()  throws RemoteException{
		pacmanx = 7 * blocksize;
		pacmany = 11 * blocksize;
		pacmandx = 0;
		pacmandy = 0;
		viewdx = -1;
		viewdy = 0;
	}
}
