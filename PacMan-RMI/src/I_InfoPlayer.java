import java.rmi.*;
 
public interface I_InfoPlayer extends Remote {

    public boolean getIngame() throws RemoteException;
    
    public void setIngame(boolean b) throws RemoteException;
    
    public boolean getDying() throws RemoteException;
    
    public void setDying(boolean b) throws RemoteException;
       
    public int getScore() throws RemoteException;
    
    
    public void setScore(int i) throws RemoteException;
        
    
    public int getViewdx() throws RemoteException;
    
    
    public void setViewdx(int i) throws RemoteException;
    
    
    public int getViewdy() throws RemoteException;
    
    
    public void setViewdy(int i) throws RemoteException;
    

	public int getPacmanx()  throws RemoteException;


	public void setPacmanx(int pacmanx)  throws RemoteException;


	public int getPacmany()  throws RemoteException;


	public void setPacmany(int pacmany)  throws RemoteException;


	public int getPacmandx()  throws RemoteException;


	public void setPacmandx(int pacmandx)  throws RemoteException;


	public int getPacmandy()  throws RemoteException;


	public void setPacmandy(int pacmandy)  throws RemoteException;


	public int getPacsleft()  throws RemoteException;


	public void setPacsleft(int pacsleft)  throws RemoteException;


	public int getReqdx()  throws RemoteException;


	public void setReqdx(int reqdx)  throws RemoteException;


	public int getReqdy()  throws RemoteException;


	public void setReqdy(int reqdy)  throws RemoteException;


	public void DrawPacMan()  throws RemoteException;


	public void setPlayer(Player p)  throws RemoteException;


	public void drawGhost(int i, int j)  throws RemoteException;


	public int getPacmanspeed()  throws RemoteException;


}