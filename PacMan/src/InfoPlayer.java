


public class InfoPlayer {

	
    private boolean ingame = false;
    private boolean dying = false;
    private final int pacmanspeed = 6;
    private int pacsleft, score;
    private int pacmanx, pacmany, pacmandx, pacmandy;
    private Player player = null;
    
    private int viewdx, viewdy, reqdx, reqdy;

    public boolean getIngame(){
    	return ingame;
    }
    
    public void setIngame(boolean b){
    	ingame = b;
    }

    public boolean getDying(){
    	return dying;
    }
    
    public void setDying(boolean b){
    	dying = b;
    }
    
    public int getScore(){
    	return score;
    }
    
    public void setScore(int i){
    	score = i;
    }    
    
    public int getViewdx(){
    	return viewdx;
    }
    
    public void setViewdx(int i){
    	viewdx = i;
    }
    
    public int getViewdy(){
    	return viewdy;
    }
    
    public void setViewdy(int i){
    	viewdy = i;
    }

	public int getPacmanx() {
		return pacmanx;
	}

	public void setPacmanx(int pacmanx) {
		this.pacmanx = pacmanx;
	}

	public int getPacmany() {
		return pacmany;
	}

	public void setPacmany(int pacmany) {
		this.pacmany = pacmany;
	}

	public int getPacmandx() {
		return pacmandx;
	}

	public void setPacmandx(int pacmandx) {
		this.pacmandx = pacmandx;
	}

	public int getPacmandy() {
		return pacmandy;
	}

	public void setPacmandy(int pacmandy) {
		this.pacmandy = pacmandy;
	}

	public int getPacsleft() {
		return pacsleft;
	}

	public void setPacsleft(int pacsleft) {
		this.pacsleft = pacsleft;
	}

	public int getReqdx() {
		return reqdx;
	}

	public void setReqdx(int reqdx) {
		this.reqdx = reqdx;
	}

	public int getReqdy() {
		return reqdy;
	}

	public void setReqdy(int reqdy) {
		this.reqdy = reqdy;
	}

	public void DrawPacMan() {
		player.DrawPacMan();
	}

	public void setPlayer(Player p) {
		player = p;
	}

	public void drawGhost(int i, int j) {
		player.drawGhost(i, j);
	}

	public int getPacmanspeed() {
		return pacmanspeed;
	}
    
}
