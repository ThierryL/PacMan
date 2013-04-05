import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class InfoPlayer extends UnicastRemoteObject implements I_InfoPlayer{

	
    private boolean ingame = false;
    private boolean dying = false;
    private final int pacmanspeed = 6;
    private int pacsleft, score;
    private int pacmanx, pacmany, pacmandx, pacmandy;
    private Player player = null;
    
    private int viewdx, viewdy, reqdx, reqdy;

    public InfoPlayer() throws RemoteException{
	    super();
    }

    public boolean getIngame() throws RemoteException{
    	return ingame;
    }
    
    public void setIngame(boolean b) throws RemoteException{
    	ingame = b;
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

	public void DrawPacMan()  throws RemoteException{
		player.DrawPacMan();
	}

	public void setPlayer(Player p)  throws RemoteException{
		player = p;
	}

	public void drawGhost(int i, int j)  throws RemoteException{
		player.drawGhost(i, j);
	}

	public int getPacmanspeed()  throws RemoteException{
		return pacmanspeed;
	}
    
}
