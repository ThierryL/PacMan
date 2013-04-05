import java.rmi.RemoteException;
import java.rmi.Remote;

public interface I_InfoGame extends Remote{

    public void newPlayer() throws RemoteException;

    public void setServer(Server s) throws RemoteException;
    
    public boolean isBegin() throws RemoteException;

	public void setBegin(boolean begin) throws RemoteException;
    
    public void InitScreenData() throws RemoteException;
    
    public void Initghostx(int[] m) throws RemoteException;
    
    public void Initghosty(int[] m) throws RemoteException;

	public int getNrofghosts() throws RemoteException;

	public void setNrofghosts(int nrofghosts) throws RemoteException;

	public int getDeathcounter() throws RemoteException;

	public void setDeathcounter(int deathcounter) throws RemoteException;

	public int[] getGhostx() throws RemoteException;

	public void setGhostx(int[] ghostx) throws RemoteException;

	public int[] getGhosty() throws RemoteException;

	public void setGhosty(int[] ghosty) throws RemoteException;

	public short[] getScreendata() throws RemoteException;

	public void setScreendata(short[] screendata) throws RemoteException;

	public int getBlocksize() throws RemoteException;

	public int getNrofblocks() throws RemoteException;

	public int getScrsize() throws RemoteException;

	public void GameInit() throws RemoteException;

	public void PlayGame() throws RemoteException;

	public void notifyMove() throws RemoteException;
}
