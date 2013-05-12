import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class InfoPlayer extends UnicastRemoteObject implements I_InfoPlayer{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private boolean dying = false;
    private final int pacmanspeed = 6;
    private int pacsleft, score;
    private int pacmanx, pacmany, pacmandx, pacmandy;
    
    private int viewdx, viewdy, reqdx, reqdy;

    private final int blocksize = 24;

    boolean playing = false;
    boolean waiting = false;
    boolean dead = false;

	private String name="";

	public void setName(String s) {
		name = s;
	}

	public String getName() {
		return name;
	}

    public InfoPlayer(String name) throws RemoteException{
	    super();
	    this.name = name;
    }

    
    public boolean isDead() throws RemoteException{
    	return dead;
    }
    
    public void setDead(boolean b) throws RemoteException{
    	dead = b;
    }
    
    public boolean isPlaying() throws RemoteException{
    	return playing;
    }
    
    public void setPlaying(boolean b) throws RemoteException{
    	playing = b;
    }
    
    public boolean isWaiting() throws RemoteException{
    	return waiting;
    }
    
    public void setWaiting(boolean b) throws RemoteException{
    	waiting = b;
    }

    public boolean getDying() throws RemoteException{
    	return dying;
    }
    
    public void setDying(boolean b) throws RemoteException{
    	dying = b;
    }
    
    public int getScore() throws RemoteException{
    	return score;
    }
    
    public void setScore(int i) throws RemoteException{
    	score = i;
    }    
    
    public int getViewdx() throws RemoteException{
    	return viewdx;
    }
    
    public void setViewdx(int i) throws RemoteException{
    	viewdx = i;
    }
    
    public int getViewdy() throws RemoteException{
    	return viewdy;
    }
    
    public void setViewdy(int i) throws RemoteException{
    	viewdy = i;
    }

	public int getPacmanx()  throws RemoteException{
		return pacmanx;
	}

	public void setPacmanx(int pacmanx)  throws RemoteException{
		this.pacmanx = pacmanx;
	}

	public int getPacmany()  throws RemoteException{
		return pacmany;
	}

	public void setPacmany(int pacmany)  throws RemoteException{
		this.pacmany = pacmany;
	}

	public int getPacmandx()  throws RemoteException{
		return pacmandx;
	}

	public void setPacmandx(int pacmandx)  throws RemoteException{
		this.pacmandx = pacmandx;
	}

	public int getPacmandy()  throws RemoteException{
		return pacmandy;
	}

	public void setPacmandy(int pacmandy)  throws RemoteException{
		this.pacmandy = pacmandy;
	}

	public int getPacsleft()  throws RemoteException{
		return pacsleft;
	}

	public void setPacsleft(int pacsleft)  throws RemoteException{
		this.pacsleft = pacsleft;
	}

	public int getReqdx()  throws RemoteException{
		return reqdx;
	}

	public void setReqdx(int reqdx)  throws RemoteException{
		this.reqdx = reqdx;
	}

	public int getReqdy()  throws RemoteException{
		return reqdy;
	}

	public void setReqdy(int reqdy)  throws RemoteException{
		this.reqdy = reqdy;
	}



	public int getPacmanspeed()  throws RemoteException{
		return pacmanspeed;
	}

	public void playerInit()  throws RemoteException{
		pacsleft = 3;
		score = 0;
		pacmanx = 7 * blocksize;
		pacmany = 11 * blocksize;
		pacmandx = 0;
		pacmandy = 0;
		viewdx = -1;
		viewdy = 0;
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
