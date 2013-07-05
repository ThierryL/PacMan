import javax.swing.ImageIcon;
import java.util.ArrayList;
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
import java.lang.Math;

import java.rmi.Naming;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.Remote;
import java.io.*;
import java.net.*;

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

	private I_InfoPlayer player;
	private Graphics2D g2d;
	private static I_InfoGame game = null;
	private static String playerName = "";
	private static String currentAddress;
	private static String nextAddress = "";
	private static int portInUse;
	private int clientStatus = 0;

	boolean win = false;
	boolean end = false;
	boolean waiting = false;



	Image ghost;
	Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
	Image pacman3up, pacman3down, pacman3left, pacman3right;
	Image pacman4up, pacman4down, pacman4left, pacman4right;


	Timer timer;


	public Player(String addr, String namePlayer) {

		currentAddress = addr;

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

		playerName = namePlayer;

		try {
			clientStatus = game.newPlayer(playerName);
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Récupération");
			e.printStackTrace();
		}


		try {
			player = (I_InfoPlayer) Naming.lookup("rmi://"+currentAddress+":1099/"+playerName);
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

		GetImages();

		addKeyListener(new TAdapter());
		mazecolor = new Color(5, 100, 5);
		setFocusable(true);

		d = new Dimension(400, 400);

		setBackground(Color.black);
		setDoubleBuffered(true);
		timer = new Timer(40, this);
		timer.start();

		if (clientStatus == 1) {
			System.out.println("Old PLayer");
			repaint();
		}
	}


	public void addNotify() {
		super.addNotify();
		try {
            if (clientStatus != 1) player.playerInit();
			if (!game.isPlaying()){
				game.GameInit();
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception addNotify");
			e.printStackTrace();
		}

	}


	private void reconnect() {
		game = null;
		player = null;
		try {
			game = (I_InfoGame) Naming.lookup("rmi://"+currentAddress+":1099/I_InfoGame");
		} catch (Exception e){
			System.out.println("Exception Game");
			reconnect();
		}
		try {
			game.recoverPlayer(playerName);
			player = (I_InfoPlayer) Naming.lookup("rmi://"+currentAddress+":1099/"+playerName);
		} catch (Exception e){
			System.out.println("Exception Player");
			reconnect();
		}
		repaint();
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


	public void ShowDecoScreen() {
		try{

			int scrsize = 48;

			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
			g2d.setColor(Color.white);
			g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

			String s;
			String s1;
			s ="              Pause        ";
			s1="Press space to relaunch";

			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g2d.setColor(Color.white);
			g2d.setFont(small);
			g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2 -10);
			g2d.drawString(s1, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2 +10);
		} catch (Exception e) {
			reconnect();
		}
	}

	public void ShowPauseScreen() {
		try{
			String playerCallPause = game.getPlayerCallPause();

			int scrsize = game.getScrsize();

			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
			g2d.setColor(Color.white);
			g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

			String s;
			String s1;
			if (playerCallPause.equals(playerName)){
				s ="              Pause        ";
				s1="Press space to relaunch";
			}else{
				s = playerCallPause + " has called a pause";
				s1 ="              Please wait";

			}
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g2d.setColor(Color.white);
			g2d.setFont(small);
			g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2 -10);
			g2d.drawString(s1, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2 +10);
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Pause Screen");
			e.printStackTrace();
		}
	}
	public void ShowEndScreen() {
		try{
			int scrsize = game.getScrsize();

			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
			g2d.setColor(Color.white);
			g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

			String s = "Press s to start again, q to quit";
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g2d.setColor(Color.white);
			g2d.setFont(small);
			g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception End Screen");
			e.printStackTrace();
		}
	}

	public void ShowDeadScreen() {
		try{
			int scrsize = game.getScrsize();

			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
			g2d.setColor(Color.white);
			g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

			String s = "You are dead";
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g2d.setColor(Color.white);
			g2d.setFont(small);
			g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Dead Screen");
			e.printStackTrace();
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
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Intro Screen");
			e.printStackTrace();
		}
	}

	public void ShowWaitingScreen() {
		try{
			int scrsize = game.getScrsize();
			int n = game.getNbPlayerExpected() - game.getNbPlayerWaiting();


			g2d.setColor(new Color(0, 32, 48));
			g2d.fillRect(50, scrsize / 2 - 30, scrsize - 100, 50);
			g2d.setColor(Color.white);
			g2d.drawRect(50, scrsize / 2 - 30, scrsize - 100, 50);

			String s = "Waiting for " + n + " others players";
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g2d.setColor(Color.white);
			g2d.setFont(small);
			g2d.drawString(s, (scrsize - metr.stringWidth(s)) / 2, scrsize / 2);
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Waiting Screen");
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
			if(end) {
				if(win) s = "You won ! Score: "+score;
				else s = "You lost ! Score: "+score;
			}
			else s = "Score: " + score;
			g2d.drawString(s, scrsize / 2 - 14, scrsize + 16);
			for (i = 0; i < pacsleft; i++) {
				g2d.drawImage(pacman3left, i * 28 + 8, scrsize + 1, this);
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw Score");
			e.printStackTrace();
		}
	}


	public void drawGhost() {

		try{
			int nbrGhosts = game.getNrofghosts();
			int[] ghostX = game.getGhostx();
			int[] ghostY = game.getGhosty();

			for(int k = 0; k<nbrGhosts; k++){
				g2d.drawImage(ghost, ghostX[k]+1, ghostY[k]+1, this);
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw Ghost");
			e.printStackTrace();
		}
	}

	public void DrawPacMan() {
		try{
			ArrayList<I_InfoPlayer> players = game.getPlayers();
			for(int i = 0; i<players.size(); i++) {
				if (players.get(i).isPlaying()) {
					if (players.get(i).getViewdx() == -1)
						DrawPacManLeft(players.get(i));
					else if (players.get(i).getViewdx() == 1)
						DrawPacManRight(players.get(i));
					else if (players.get(i).getViewdy() == -1)
						DrawPacManUp(players.get(i));
					else
						DrawPacManDown(players.get(i));
				}
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw PacMan");
			e.printStackTrace();
		}

	}

	public void DrawPacManUp(I_InfoPlayer p) {    	
		try{
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2up, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3up, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4up, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw PacMan Up");
			e.printStackTrace();
		}

	}


	public void DrawPacManDown(I_InfoPlayer p) {
		try {
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2down, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3down, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4down, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw PacMan Down");
			e.printStackTrace();
		}

	}


	public void DrawPacManLeft(I_InfoPlayer p) {
		try {
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2left, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3left, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4left, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw PacMan Left");
			e.printStackTrace();
		}

	}


	public void DrawPacManRight(I_InfoPlayer p) {
		try{
			switch (pacmananimpos) {
			case 1:
				g2d.drawImage(pacman2right, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 2:
				g2d.drawImage(pacman3right, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			case 3:
				g2d.drawImage(pacman4right, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			default:
				g2d.drawImage(pacman1, p.getPacmanx() + 1, p.getPacmany() + 1, this);
				break;
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw PacMan Right");
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
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Draw Maze");
			e.printStackTrace();
		}
	}


	public void GetImages()
	{

		ghost = new ImageIcon(Player.class.getResource("./ghost.gif")).getImage();
		pacman1 = new ImageIcon(Player.class.getResource("./pacman.gif")).getImage();
		pacman2up = new ImageIcon(Player.class.getResource("./up1.gif")).getImage();
		pacman3up = new ImageIcon(Player.class.getResource("./up2.gif")).getImage();
		pacman4up = new ImageIcon(Player.class.getResource("./up3.gif")).getImage();
		pacman2down = new ImageIcon(Player.class.getResource("./down1.gif")).getImage();
		pacman3down = new ImageIcon(Player.class.getResource("./down2.gif")).getImage(); 
		pacman4down = new ImageIcon(Player.class.getResource("./down3.gif")).getImage();
		pacman2left = new ImageIcon(Player.class.getResource("./left1.gif")).getImage();
		pacman3left = new ImageIcon(Player.class.getResource("./left2.gif")).getImage();
		pacman4left = new ImageIcon(Player.class.getResource("./left3.gif")).getImage();
		pacman2right = new ImageIcon(Player.class.getResource("./right1.gif")).getImage();
		pacman3right = new ImageIcon(Player.class.getResource("./right2.gif")).getImage();
		pacman4right = new ImageIcon(Player.class.getResource("./right3.gif")).getImage();

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
			if(game.moving()) {
				game.stopped();
				changeServer();
			}
			if(game.isPause() ) {
				ShowPauseScreen();
			}else if (game.isEnded() && !game.isWaiting()) {
				ShowEndScreen();
			} else if (game.isPlaying() && player.isPlaying()){
				PlayGame();
			} else if (game.isPlaying() && player.isDead()){
				drawGhost();
				DrawPacMan();
				ShowDeadScreen();
			} else if (game.isWaiting()&& player.isWaiting()){
				ShowWaitingScreen();
			} else {
				ShowIntroScreen();
			}

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Paint");
			e.printStackTrace();
		}

		g.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private void waitingForNewServer(int port) {
		String info = "";
		Socket clientSocket = null;
		InetAddress inetAddress = null;
		DataOutputStream out = null;
		BufferedReader in = null;
		try {
			inetAddress = InetAddress.getByName(currentAddress);
			clientSocket = new Socket(inetAddress, port);
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while(!info.equals("moved"))  info = in.readLine();
			out.writeBytes("");
			clientSocket.close();
		} catch (Exception e){
			//reconnect();
			System.out.println("Failed socket to server");
			waitingForNewServer(port);
		}
	}

	private void changeServer() {
		int clientPort = 0; 
		try{
			clientPort = game.getClientPort();
			nextAddress = game.getNextAddress();
		} catch (RemoteException e){
			System.out.println("Remote error");
			System.exit(1);
		}
		try {  
			while(!game.unbind());
			player = null;
			game.unbinding(playerName);
			game = null;

		}  catch (Exception e) {  
			System.out.println("Failed To unbind object with the RMI registry");  
		}

		try{
			waitingForNewServer(clientPort);

			game = (I_InfoGame) Naming.lookup("rmi://"+nextAddress+":1099/I_InfoGame");
			player = (I_InfoPlayer) Naming.lookup("rmi://"+nextAddress+":1099/"+playerName);
			currentAddress = nextAddress;
			game.playerConnected();
			while(game.moving());

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

	public void PlayGame(){
		try {
			if (player.getDying()) {
				Death();
			} else {
                System.out.println("On entre dans le try");
				MovePacMan();
                System.out.println("On bouge");
				DrawPacMan();
                System.out.println("On dessine");
				game.moveGhosts();
                System.out.println("On bouge les fantomes");
				CheckPlayerKilled();
                System.out.println("On check");
				drawGhost();
                System.out.println("On dessine les fantomes");
				CheckMaze();
                System.out.println("On check");
			}

			game.saveConf(playerName);

		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception PlayGame");
			e.printStackTrace();
		}

	}

	public void CheckMaze() {
		short i = 0;
		boolean finished = true;
		try {
			int nrofblocks = game.getNrofblocks();
			int nrofghosts = game.getNrofghosts();

			while (i < nrofblocks * nrofblocks && finished) {
				if ((game.getScreendata()[i] & 48) != 0)
					finished = false;
				i++;
			}

			if (finished) {
				player.setScore(player.getScore()+50);

				//game.finished();
				game.levelContinue();
				game.setEnded(true);
				game.setPlaying(false);



			}
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Check Maze");
			e.printStackTrace();
		}
	}

	public void CheckPlayerKilled() {
		short i;
		try {
			int[] ghosty = game.getGhosty();
			int[] ghostx = game.getGhostx();
			int nrofghosts = game.getNrofghosts();



			for (i = 0; i < nrofghosts; i++) {

				if (player.getPacmanx() > (ghostx[i] - 12) && player.getPacmanx() < (ghostx[i] + 12) &&
						player.getPacmany() > (ghosty[i] - 12) && player.getPacmany() < (ghosty[i] + 12) &&
						player.isPlaying()) {

					player.setDying(true);
					game.setDeathcounter(game.getDeathcounter() +64);

				}
			}
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Check Player Killed");
			e.printStackTrace();
		}

	}

	public void MovePacMan() {
		int pos;
		short ch;

		try {
			int blocksize = game.getBlocksize();
			int nrofblocks = game.getNrofblocks();
			short[] screendata = game.getScreendata();

			int reqdx = player.getReqdx();
			int reqdy = player.getReqdy();


			if (reqdx == -player.getPacmandx() && reqdy == -player.getPacmandy()) {
				player.setPacmandx(reqdx);
				player.setPacmandy(reqdy);
				player.setViewdx(reqdx);
				player.setViewdy(reqdy);
			}
			if (player.getPacmanx() % blocksize == 0 && player.getPacmany() % blocksize == 0) {
				pos = player.getPacmanx() / blocksize + nrofblocks * (int)(player.getPacmany() / blocksize);
				ch = screendata[pos];

				if ((ch & 16) != 0) {
					game.setScreendata((short)(ch & 15),pos);
					player.setScore(player.getScore()+1);
				}

				if (reqdx != 0 || reqdy != 0) {
					if (!((reqdx == -1 && reqdy == 0 && (ch & 1) != 0) ||
							(reqdx == 1 && reqdy == 0 && (ch & 4) != 0) ||
							(reqdx == 0 && reqdy == -1 && (ch & 2) != 0) ||
							(reqdx == 0 && reqdy == 1 && (ch & 8) != 0))) {
						player.setPacmandx(reqdx);
						player.setPacmandy(reqdy);
						player.setViewdx(reqdx);
						player.setViewdy(reqdy);
					}
				}

				// Check for standstill
				if ((player.getPacmandx() == -1 && player.getPacmandy() == 0 && (ch & 1) != 0) ||
						(player.getPacmandx() == 1 && player.getPacmandy() == 0 && (ch & 4) != 0) ||
						(player.getPacmandx() == 0 && player.getPacmandy() == -1 && (ch & 2) != 0) ||
						(player.getPacmandx() == 0 && player.getPacmandy() == 1 && (ch & 8) != 0)) {
					player.setPacmandx(0);
					player.setPacmandy(0);
				}
			}
			player.setPacmanx(player.getPacmanx() + player.getPacmanspeed() * player.getPacmandx());
			player.setPacmany(player.getPacmany() + player.getPacmanspeed() * player.getPacmandy());
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Move PacMan");
			e.printStackTrace();
		}
	}

	public void Death() {
		try {

			player.setPacsleft(player.getPacsleft()-1);
			if (player.getPacsleft() == 0){
				player.setPlaying(false);
				player.setWaiting(false);
				player.setDead(true);
				game.removePlayerPlaying();
			}
			player.setDying(false);
			player.playerInitPos();

			//We check if everyone is dead or not
			if (game.allDead() == true) {
				game.levelContinue();
				game.setEnded(true);
				game.setPlaying(false);
			}
		} catch (RemoteException re){
			reconnect();
		}catch (Exception e) {
			System.out.println("Exception Death");
			e.printStackTrace();
		}
	}

	public void exit() {
		try{
			game.exit(player.getName());
		} catch (RemoteException re){
			reconnect();
		} catch (Exception e) {
			System.out.println("Exception Exit");
			e.printStackTrace();
		}
	}

	public void win() {
		//	win = true;
		//	end = true;
		//	try {
		//	player.setIngame(false);
		//	} catch (Exception e) {
		//		reconnect();
		//	}
	}

	public void loose() {
		//	win = false;
		//	end = true;
		//	try {
		//	player.setIngame(false);
		//	} catch (Exception e) {
		//		e.printStackTrace();
		//	}
	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			int reqdx = 0;
			int reqdy = 0;
			try {
				if (player.isPlaying())
				{
					if (key == 'u' || key == 'U')
					{
						game.disconnect(playerName);
						game = null;
						player = null;
					}

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
						exit();
						System.exit(0);
					}
					else if (key == KeyEvent.VK_SPACE) {
						if (game.isPause())
							game.setPause(false, playerName);
						else {
							game.setPause(true, playerName);
						}
					}
				}
				else
				{
					if (key == 's' || key == 'S')
					{
						if (game.isWaiting()&&!player.isWaiting()) {
							player.playerInit();
							player.setWaiting(true);
							game.addPlayerWaiting();
						} else if(game.isPlaying()){
							player.playerInit();
							player.setPlaying(true);
							game.addPlayerPlaying();
						} else if(game.isEnded()){
							game.restart();

						}
					}
					if (key == KeyEvent.VK_Q)
					{
						exit();
						System.exit(0);
					}
				}

			} catch (RemoteException re){
				reconnect();
			} catch (Exception ex) {
				System.out.println("Exception Key Pressed");
				ex.printStackTrace();
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

				} catch (RemoteException re){
					reconnect();
				} catch (Exception ex) {
					System.out.println("Exception Key Released");
					ex.printStackTrace();
				}

			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  
	}
}
