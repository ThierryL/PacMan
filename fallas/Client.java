import javax.swing.JFrame;
import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

public class Client extends JFrame
{
	private static I_InfoGame game = null;
	private Player player;
	private static String currentAddress;
	
	public Client(){
			player = new Player(game,currentAddress);
			add(player);
			setTitle("Pacman");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(380, 420);
			setLocationRelativeTo(null);
			setVisible(true);
	}
	
	static public void main(String[] args){
		if (args.length != 1) {
			System.out.println("Wrong number of argument");
			System.exit(1);
		}
		currentAddress = args[0];

		try{
			game = (I_InfoGame) Naming.lookup("rmi://"+currentAddress+":1099/I_InfoGame");
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

		new Client();
	}
}
