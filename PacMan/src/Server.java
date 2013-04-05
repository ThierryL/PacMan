import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;




public class Server {


    final int maxghosts = 12;
    int[] dx, dy;
    int[] ghostdx, ghostdy, ghostspeed;
    

    InfoGame game;

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

    	game = new InfoGame();
    	game.setServer(this);
    	game.InitScreenData();
        game.Initghostx(new int[maxghosts]);
        ghostdx = new int[maxghosts];
        game.Initghosty(new int[maxghosts]);
        ghostdy = new int[maxghosts];
        ghostspeed = new int[maxghosts];
        dx = new int[4];
        dy = new int[4];
        Client c = new Client(game);
    }

    public void PlayGame(ArrayList<InfoPlayer> players) {
	    try {
		    for(int i = 0; i<players.size(); i++){
			    if (players.get(i).getDying()) {
				    Death(players.get(i));
			    } else {
				    MovePacMan(players.get(i));
				    players.get(i).DrawPacMan();
				    moveGhosts(players.get(i));
				    CheckMaze(players.get(i));
			    }
		    }
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
    }
        
    

    public void CheckMaze(InfoPlayer player) {
        short i = 0;
        boolean finished = true;

        int nrofblocks = game.getNrofblocks();
        int nrofghosts = game.getNrofghosts();
        
        while (i < nrofblocks * nrofblocks && finished) {
            if ((game.getScreendata()[i] & 48) != 0)
                finished = false;
            i++;
        }

        if (finished) {
		try {
			player.setScore(player.getScore()+50);

			if (nrofghosts < maxghosts)
				game.setNrofghosts(nrofghosts++);
			if (currentspeed < maxspeed)
				currentspeed++;
			LevelInit(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
        }
    }
    
    public void Death(InfoPlayer player) {
	    try {

		    player.setPacsleft(player.getPacsleft()-1);
		    if (player.getPacsleft() == 0)
			    player.setIngame(false);
		    LevelContinue(player);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
    }


    public void moveGhosts(InfoPlayer player) {
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
	    try {
		    player.drawGhost(ghostx[i] + 1, ghosty[i] + 1);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }

	    try {
		    if (player.getPacmanx() > (ghostx[i] - 12) && player.getPacmanx() < (ghostx[i] + 12) &&
				    player.getPacmany() > (ghosty[i] - 12) && player.getPacmany() < (ghosty[i] + 12) &&
				    player.getIngame()) {

			    player.setDying(true);
			    game.setDeathcounter(game.getDeathcounter() +64);

				    }
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
	}
    }
    

public void MovePacMan(InfoPlayer player) {
	int pos;
	short ch;

	int blocksize = game.getBlocksize();
	int nrofblocks = game.getNrofblocks();
	short[] screendata = game.getScreendata();

	try {
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
				screendata[pos] = (short)(ch & 15);
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


	} catch (Exception e) {
		e.printStackTrace();
	}

	game.setScreendata(screendata);
}


public void GameInit(InfoPlayer player) {
	try {
		player.setPacsleft(3);
		player.setScore(0);
		LevelInit(player);

	} catch (Exception e) {
		e.printStackTrace();
	}

	game.setNrofghosts(6);
	currentspeed = 3;
}


public void LevelInit(InfoPlayer player) {
	try{

		game.setScreendata(leveldata);
		LevelContinue(player);

	} catch (Exception e) {
		e.printStackTrace();
	}

}


    public void LevelContinue(InfoPlayer player) {
        short i;
        int dx = 1;
        int random;

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

	try {
		player.setPacmanx(7 * blocksize);
		player.setPacmany(11 * blocksize);
		player.setPacmandx(0);
		player.setPacmandy(0);
		player.setViewdx(-1);
		player.setViewdy(0);
		player.setDying(false);

	} catch (Exception e) {
		e.printStackTrace();
	}

    }

    
    public static void main(String[] args) {
        new Server();
    }

}
