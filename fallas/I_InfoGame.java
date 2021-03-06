import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.ArrayList;

public interface I_InfoGame extends Remote{

    public void newPlayer(String name) throws RemoteException;
    
    public void disconnect(String name) throws RemoteException;

    public void moveGhosts() throws RemoteException;

    public void levelContinue() throws RemoteException;

	public  ArrayList<I_InfoPlayer> getPlayers() throws RemoteException;

	public void exit(String name) throws RemoteException;
	
	public boolean checkInit() throws RemoteException;

	public void addPlayerWaiting() throws RemoteException;

	public int getNbPlayerWaiting() throws RemoteException;

	public void addPlayerPlaying() throws RemoteException;
	
	public void removePlayerPlaying() throws RemoteException;

	public int getNbPlayerExpected() throws RemoteException;

	public int getNbrPlayerInGame() throws RemoteException;
	
	public void finished() throws RemoteException;

    public void setServer(Server s) throws RemoteException;

    public boolean isEnded()  throws RemoteException;

	public void setEnded(boolean ended)  throws RemoteException;
    
    public boolean isPlaying() throws RemoteException;

	public void setPlaying(boolean playing) throws RemoteException;
    
    public boolean isPause() throws RemoteException;

	public void setPause(boolean playing, String player) throws RemoteException;

	public String getPlayerCallPause() throws RemoteException;

    public boolean isWaiting() throws RemoteException;

	public void setWaiting(boolean waiting) throws RemoteException;
    
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

	public void setScreendata(short screendata, int i) throws RemoteException;

	public int getBlocksize() throws RemoteException;

	public int getNrofblocks() throws RemoteException;

	public int getScrsize() throws RemoteException;

	public void GameInit() throws RemoteException;

	public boolean allDead() throws RemoteException;

	public void restart() throws RemoteException;

	public void construction(String s) throws RemoteException;

	public boolean moving() throws RemoteException;

	public void setMoving(boolean b) throws RemoteException;

	public void setNextAddress(String b) throws RemoteException;

	public String getNextAddress() throws RemoteException;

	public void unbinding(String s) throws RemoteException;

	public boolean getUnbinding() throws RemoteException;

	public boolean unbind() throws RemoteException;

	public void setUnbind(boolean b) throws RemoteException;

	public void setServerAddress(String ip) throws RemoteException;

	public boolean gameStopped() throws RemoteException;

	public void stopped() throws RemoteException;

	public void playerConnected() throws RemoteException;

	public boolean allConnected() throws RemoteException;

	public int getClientPort() throws RemoteException;
	
	public void saveConf(String name) throws RemoteException;
}
