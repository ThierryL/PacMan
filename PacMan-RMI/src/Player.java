
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

public class Player extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Dimension d;
	Font smallfont = new Font("Helvetica", Font.BOLD, 14);

	FontMetrics fmsmall, fmlarge;
	Image ii;
	Color dotcolor = new Color(192, 192, 0);
	Color mazecolor;
	final int pacanimdelay = 2;
	final int pacmananimcount = 4;
	int pacanimcount = pacanimdelay;
	int pacanimdir = 1;
	int pacmananimpos = 0;

	private InfoGame game;
	private InfoPlayer player;
	private Graphics2D g2d;


	Image ghost;
	Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
	Image pacman3up, pacman3down, pacman3left, pacman3right;
	Image pacman4up, pacman4down, pacman4left, pacman4right;


	Timer timer;


	public Player() {

		try {
			game = (InfoGame) Naming.lookup("rmi://localhost:1099/InfoGame");
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

		try {
			player = new InfoPlayer();
			player.setPlayer(this);
			Naming.rebind("rmi://localhost:1099/InfoPlayer", player);
			game.newPlayer();
		} catch (MalformedURLException e){
			System.out.println("URL invalida");
			System.exit(128);
		} catch (RemoteException e){
			System.out.println("Excepcion remota tratanod de conectarse al servidor");
			System.exit(128);
		}


		GetImages();

		addKeyListener(new TAdapter());
		mazecolor = new Color(5, 100, 5);
		setFocusable(true);

		d = new Dimension(400, 400);

		setBackground(Color.black);
		setDoubleBuffered(true);
		timer = new Timer(40, this);
		timer.start();
	}


	public void addNotify() {
		super.addNotify();
		try {
			game.GameInit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void DoAnim() {
		pacanimcount--;
		if (pacanimcount <= 0) {
			pacanimcount = pacanimdelay;
			pacmananimpos = pacmananimpos + pacanimdir;
			if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0)
				pacanimdir = -pacanimdir;
		}
	}


	public void ShowIntroScreen() {
		try{
			int scrsize = game.getScrsize();

			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
			g2d.setColor(Color.white);
			g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

			String s = "Press s to start.";
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g2d.setColor(Color.white);
			g2d.setFont(small);
			g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DrawScore() {
		int i;
		String s;

		try{
			int scrsize = game.getScrsize();
			int score = player.getScore();
			int pacsleft = player.getPacsleft();



			g2d.setFont(smallfont);
			g2d.setColor(new Color(96, 128, 255));
			s = "Score: " + score;
			g2d.drawString(s, scrsize / 2 + 96, scrsize + 16);
			for (i = 0; i < pacsleft; i++) {
				g2d.drawImage(pacman3left, i * 28 + 8, scrsize + 1, this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void drawGhost(int x, int y) {
		g2d.drawImage(ghost, x, y, this);
	}

	public void DrawPacMan() {
		try{
			if (player.getViewdx() == -1)
				DrawPacManLeft();
			else if (player.getViewdx() == 1)
				DrawPacManRight();
			else if (player.getViewdy() == -1)
				DrawPacManUp();
			else
				DrawPacManDown();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void DrawPacManUp() {    	
		try{
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2up, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3up, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4up, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void DrawPacManDown() {
		try {
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2down, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3down, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4down, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void DrawPacManLeft() {
		try {
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2left, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3left, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4left, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void DrawPacManRight() {
		try{
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2right, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3right, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4right, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, player.getPacmanx() + 1, player.getPacmany() + 1, this);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void DrawMaze() {
		short i = 0;
		int x, y;

		try{
			int blocksize = game.getBlocksize();
			int scrsize = game.getScrsize();
			short[] screendata = game.getScreendata();


			for (y = 0; y < scrsize; y += blocksize) {
				for (x = 0; x < scrsize; x += blocksize) {
					g2d.setColor(mazecolor);
					g2d.setStroke(new BasicStroke(2));

					if ((screendata[i] & 1) != 0) // draws left
					{
						g2d.drawLine(x, y, x, y + blocksize - 1);
					}
					if ((screendata[i] & 2) != 0) // draws top
					{
						g2d.drawLine(x, y, x + blocksize - 1, y);
					}
					if ((screendata[i] & 4) != 0) // draws right
					{
						g2d.drawLine(x + blocksize - 1, y, x + blocksize - 1,
								y + blocksize - 1);
					}
					if ((screendata[i] & 8) != 0) // draws bottom
					{
						g2d.drawLine(x, y + blocksize - 1, x + blocksize - 1,
								y + blocksize - 1);
					}
					if ((screendata[i] & 16) != 0) // draws point
					{
						g2d.setColor(dotcolor);
						g2d.fillRect(x + 11, y + 11, 2, 2);
					}
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void GetImages()
	{

		ghost = new ImageIcon(Board.class.getResource("./ghost.gif")).getImage();
		pacman1 = new ImageIcon(Board.class.getResource("./pacman.gif")).getImage();
		pacman2up = new ImageIcon(Board.class.getResource("./up1.gif")).getImage();
		pacman3up = new ImageIcon(Board.class.getResource("./up2.gif")).getImage();
		pacman4up = new ImageIcon(Board.class.getResource("./up3.gif")).getImage();
		pacman2down = new ImageIcon(Board.class.getResource("./down1.gif")).getImage();
		pacman3down = new ImageIcon(Board.class.getResource("./down2.gif")).getImage(); 
		pacman4down = new ImageIcon(Board.class.getResource("./down3.gif")).getImage();
		pacman2left = new ImageIcon(Board.class.getResource("./left1.gif")).getImage();
		pacman3left = new ImageIcon(Board.class.getResource("./left2.gif")).getImage();
		pacman4left = new ImageIcon(Board.class.getResource("./left3.gif")).getImage();
		pacman2right = new ImageIcon(Board.class.getResource("./right1.gif")).getImage();
		pacman3right = new ImageIcon(Board.class.getResource("./right2.gif")).getImage();
		pacman4right = new ImageIcon(Board.class.getResource("./right3.gif")).getImage();

	}

	public void paint(Graphics g)
	{
		super.paint(g);

		g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, d.width, d.height);

		DrawMaze();
		DrawScore();
		DoAnim();
		try {
			if (player.getIngame())
				game.PlayGame();
			else
				ShowIntroScreen();

		} catch (Exception e) {
			e.printStackTrace();
		}




		g.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			int reqdx = 0;
			int reqdy = 0;
			try {

				if (player.getIngame())
				{
					if (key == KeyEvent.VK_LEFT)
					{
						reqdx=-1;
						reqdy=0;
						player.setReqdx(reqdx);
						player.setReqdy(reqdy);
					}
					else if (key == KeyEvent.VK_RIGHT)
					{
						reqdx=1;
						reqdy=0;
						player.setReqdx(reqdx);
						player.setReqdy(reqdy);
					}
					else if (key == KeyEvent.VK_UP)
					{
						reqdx=0;
						reqdy=-1;
						player.setReqdx(reqdx);
						player.setReqdy(reqdy);
					}
					else if (key == KeyEvent.VK_DOWN)
					{
						reqdx=0;
						reqdy=1;
						player.setReqdx(reqdx);
						player.setReqdy(reqdy);
					}
					else if (key == KeyEvent.VK_ESCAPE && timer.isRunning())
					{
						player.setIngame(false);
					}
					else if (key == KeyEvent.VK_PAUSE) {
						if (timer.isRunning())
							timer.stop();
						else timer.start();
					}
				}
				else
				{
					if (key == 's' || key == 'S')
					{
						player.setIngame(true);
						game.GameInit();
					}
				}

			} catch (Exception exc) {
				exc.printStackTrace();
			}

		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == Event.LEFT || key == Event.RIGHT || 
					key == Event.UP ||  key == Event.DOWN)
			{
				try {
					player.setReqdx(0);
					player.setReqdy(0);

				} catch (Exception exc) {
					exc.printStackTrace();
				}

			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  
	}
}
