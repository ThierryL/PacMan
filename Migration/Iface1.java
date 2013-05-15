import java.rmi.RemoteException;
import java.rmi.Remote;

public interface Iface1 extends Remote{

	public double power2(int x) throws RemoteException;

	public void sort(int[] a) throws RemoteException;

	public int[] sortReal(int[] a) throws RemoteException;

	public boolean moving() throws RemoteException;

	public boolean changing() throws RemoteException;

	public String moveTo() throws RemoteException;

	public void setMoving(boolean b) throws RemoteException;

	public void setChanging(boolean b) throws RemoteException;

	public void setNextAddress(String b) throws RemoteException;

	public void setNextPort(int b) throws RemoteException;

	public int getNextPort() throws RemoteException;

	public String getNextAddress() throws RemoteException;

	public void unbinding() throws RemoteException;

	public int getUnbinding() throws RemoteException;

}
