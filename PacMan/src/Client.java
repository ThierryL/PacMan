
import javax.swing.JFrame;

public class Client extends JFrame
{

  public Client(InfoGame game)
  {
	InfoPlayer player = game.createInfoPlayer();
    add(new Player(game,player));
    setTitle("Pacman");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(380, 420);
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
