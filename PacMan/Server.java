import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;




public class Server {

	boolean isInitialized = false;
	private boolean playing = false;

	int numberFinished;
	final int maxghosts = 12;
	int[] dx, dy;
	int[] ghostdx, ghostdy, ghostspeed;

	//ghostMove serve to know when we have to move ghosts
	//It's used in PlayGame
	int ghostMove = 0;


	private static InfoGame game;
	private static I_InfoPlayer player = null;

	final short leveldata[] =
		{ 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
			21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
			21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 
			21, 0,  0,  0,  17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 
			17, 18, 18, 18, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 20,
			17, 16, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 16, 24, 20, 
			25, 16, 16, 16, 24, 24, 28, 0,  25, 24, 24, 16, 20, 0,  21, 
			1,  17, 16, 20, 0,  0,  0,  0,  0,  0,  0,  17, 20, 0,  21,
			1,  17, 16, 16, 18, 18, 22, 0,  19, 18, 18, 16, 20, 0,  21,
			1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21, 
			1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21,
			1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0,  21,
			1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  21,
			1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
			9,  8,  8,  8,  8,  8,  8,  8,  8,  8,  25, 24, 24, 24, 28 };


	final int validspeeds[] = { 1, 2, 3, 4, 6, 8 };
	final int maxspeed = 6;
	int currentspeed = 3;



	public Server() {

			game.setServer(this);
			game.InitScreenData();
			game.Initghostx(new int[maxghosts]);
			ghostdx = new int[maxghosts];
			game.Initghosty(new int[maxghosts]);
			ghostdy = new int[maxghosts];
			ghostspeed = new int[maxghosts];
			dx = new int[4];
			dy = new int[4];

			numberFinished = 0;
	}

	public void moveGhosts() {
		try {
			int nbrPlayerInGame = game.getNbrPlayerInGame();	
			if (ghostMove==0){
				short i;
				int pos;
				int count;
				int blocksize = game.getBlocksize();
				int[] ghosty = game.getGhosty();
				int[] ghostx = game.getGhostx();
				int nrofblocks = game.getNrofblocks();
				int nrofghosts = game.getNrofghosts();
				short[] screendata = game.getScreendata();



				for (i = 0; i < nrofghosts; i++) {
					if (ghostx[i] % blocksize == 0 && ghosty[i] % blocksize == 0) {
						pos =
							ghostx[i] / blocksize + nrofblocks * (int)(ghosty[i] / blocksize);

						count = 0;
						if ((screendata[pos] & 1) == 0 && ghostdx[i] != 1) {
							dx[count] = -1;
							dy[count] = 0;
							count++;
						}
						if ((screendata[pos] & 2) == 0 && ghostdy[i] != 1) {
							dx[count] = 0;
							dy[count] = -1;
							count++;
						}
						if ((screendata[pos] & 4) == 0 && ghostdx[i] != -1) {
							dx[count] = 1;
							dy[count] = 0;
							count++;
						}
						if ((screendata[pos] & 8) == 0 && ghostdy[i] != -1) {
							dx[count] = 0;
							dy[count] = 1;
							count++;
						}

						if (count == 0) {
							if ((screendata[pos] & 15) == 15) {
								ghostdx[i] = 0;
								ghostdy[i] = 0;
							} else {
								ghostdx[i] = -ghostdx[i];
								ghostdy[i] = -ghostdy[i];
							}
						} else {
							count = (int)(Math.random() * count);
							if (count > 3)
								count = 3;
							ghostdx[i] = dx[count];
							ghostdy[i] = dy[count];
						}

					}
					ghostx[i] = ghostx[i] + (ghostdx[i] * ghostspeed[i]);
					ghosty[i] = ghosty[i] + (ghostdy[i] * ghostspeed[i]);


				}
			}
			ghostMove =(ghostMove + 1) % nbrPlayerInGame; 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void GameInit() {
		if (!isInitialized){
			//	if (!playing) {
			//		playing = true;
			LevelInit();
			try {
				game.setNrofghosts(6);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentspeed = 3;
			//}
		}
	}


	public void LevelInit() {
		try{
			for(int i = 0; i<game.getNrofblocks()*game.getNrofblocks(); i++)
				game.setScreendata(leveldata[i],i);
			LevelContinue();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void LevelContinue() {
		short i;
		int dx = 1;
		int random;

		try {
			int blocksize = game.getBlocksize();
			int[] ghosty = game.getGhosty();
			int[] ghostx = game.getGhostx();
			int nrofghosts = game.getNrofghosts();

			for (i = 0; i < nrofghosts; i++) {
				ghosty[i] = 4 * blocksize;
				ghostx[i] = 4 * blocksize;
				ghostdy[i] = 0;
				ghostdx[i] = dx;
				dx = -dx;
				random = (int)(Math.random() * (currentspeed + 1));
				if (random > currentspeed)
					random = currentspeed;
				ghostspeed[i] = validspeeds[random];
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	public static void main(String[] args){
		// Establecimiento del stub en el rmiserver
		try{
			// Crear el stub (objeto distribuido)
			game = new InfoGame();

			// Hacer bind de la instancia en el servidor rmi
			Naming.rebind("rmi://localhost:1099/I_InfoGame", game);
			new Server();
		} catch (RemoteException e){
			System.out.println("Hubo una excepcion creando la instancia del objeto distribuido");
		} catch (MalformedURLException e){
			System.out.println("URL mal formada al tratar de publicar el objeto");
		}

		if (args.length == 2){
			int arg = Integer.parseInt(args[1]);
			game.setNbPlayerExpected(arg);
		}

	}

	public void finished() {
		int numberPlayer = game.getNumberPlayer();
		numberFinished += 1;
		if(numberFinished == numberPlayer){
			//Ghosts update
			int nrOfGhosts = game.getNrofghosts();
				if (nrOfGhosts < maxghosts){
					game.setNrofghosts(nrOfGhosts++);
				}
				if (currentspeed < maxspeed){
					currentspeed++;
				}
				LevelInit();
		}
	}
}
