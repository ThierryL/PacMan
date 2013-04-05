
import javax.swing.JFrame;
import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

public class Client extends JFrame
{
	private static I_InfoGame game = null;
	
	public Client(){
		try{
			InfoPlayer player = game.createInfoPlayer();
			add(new Player((InfoGame) game,player));
			setTitle("Pacman");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(380, 420);
			setLocationRelativeTo(null);
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void main(String[] args){

		try{
			game = (InfoGame) Naming.lookup("rmi://localhost:1099/InfoGame");
			new Client();
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
	}
}
