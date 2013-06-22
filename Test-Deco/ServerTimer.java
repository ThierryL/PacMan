public class ServerTimer implements Runnable {

	private boolean shutDown = false;
	private boolean finished = false;
    private int time = 0;
	
	public ServerTimer(int t) {
        time = t*1000;
	}

	public boolean finished() {
		if (finished) System.out.print("");
		return finished;
	}

	public void shutDown() {
		shutDown = true;
	}	

	public void run() {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
		}
		finished = true;
		while(!shutDown);
	}
}
