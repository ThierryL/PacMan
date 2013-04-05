
import javax.swing.JFrame;

public class Client extends JFrame
{

<<<<<<< HEAD
  public Client(InfoGame game){

=======
  public Client(InfoGame game)
  {
>>>>>>> 04b4d04355ae6391458db7ba466760cdff1b88a7
	InfoPlayer player = game.createInfoPlayer();
    add(new Player(game,player));
    setTitle("Pacman");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(380, 420);
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
