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
	
	public Client(String namePlayer){
			player = new Player(game,currentAddress, namePlayer);
			add(player);
			setTitle("Pacman");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(380, 420);
			setLocationRelativeTo(null);
			setVisible(true);
	}
	
	static public void main(String[] args){
		String namePlayer = "";
		if (args.length == 1) {
			currentAddress = args[0];
		}else if (args.length == 2) {
			currentAddress = args[0];
			namePlayer=args[1];
		}else{
			System.out.println("Wrong number of argument");
			System.exit(1);
		}

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

		new Client(namePlayer);
	}
}
