import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class IfaceImpl1 extends UnicastRemoteObject implements Iface1{

	private String newAddress = "";
	private int nextPort = 0;
	private boolean moving = false;
	private boolean changing = false;
	private int unbinding = 0;

	public IfaceImpl1() throws RemoteException{
		super();
	}

	public double power2(int x) throws RemoteException{
                return Math.pow(2.0, (double) x);
        }

        public void sort(int[] a) throws RemoteException{
                Arrays.sort(a);

		System.out.println(Arrays.toString(a));
        }

        public int[] sortReal(int[] a) throws RemoteException{
                int[] b = new int[a.length];

                b = Arrays.copyOf(a, a.length);
                Arrays.sort(b);
                return b;
        }

	public void unbinding() {
		unbinding += 1;
	}

	public int getUnbinding() {
		return unbinding;
	}

	public boolean moving() throws RemoteException{
		return moving;
	}

	public boolean changing() throws RemoteException{
		return changing;
	}

	public String moveTo() throws RemoteException{
		return newAddress;
	}

	public void setMoving(boolean b) throws RemoteException{
		moving = b;
	}

	public void setChanging(boolean b) throws RemoteException{
		changing = b;
	}

	public void setNextAddress(String ad) throws RemoteException{
		newAddress = ad;
	}

	public void setNextPort(int port) throws RemoteException{
		nextPort = port;
	}

	public int getNextPort() throws RemoteException{
		return nextPort;
	}

	public String getNextAddress() throws RemoteException{
		return newAddress;
	}


}
