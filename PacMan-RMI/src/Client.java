
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
			add(new Player());
			setTitle("Pacman");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(380, 420);
			setLocationRelativeTo(null);
			setVisible(true);
	}
	
	static public void main(String[] args){
			new Client();
	}
}
