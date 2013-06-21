public class ServerTimer implements Runnable {

	private boolean shutDown = false;
	private boolean finished = false;
	
	public ServerTimer() {
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
			Thread.sleep(60000);
		} catch(InterruptedException e) {
		}
		finished = true;
		while(!shutDown);
	}
}
