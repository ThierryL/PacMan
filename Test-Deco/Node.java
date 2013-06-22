public class Node {

	private String name;
	private String ipAddr;
	private int socketPort;
	private int modPort;
	private int socketClient;
	private int modClient;
	private Node next;

	public Node() {
		name = "";
		ipAddr = "";
		socketPort = 0;
		modPort = 0;
		socketClient = 0;
		modClient = 0;
		next = null;
	}

	public Node(String line) {
		String[] info = line.split(" ");
		name = info[0];
		ipAddr = info[1];
		socketPort = Integer.parseInt(info[2]);
		socketClient = Integer.parseInt(info[3]);
		next = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ip) {
		this.ipAddr = ip;
	}

	public int getPortClient() {
		modClient = (modClient + 1) % 10;
		return socketClient + modClient;
	}

	public int getPort() {
		modPort = (modPort + 1) % 10;
		return socketPort + modPort;
	}

	public void setPort(int port) {
		this.socketPort = port;
	}
	
	public Node getNext() {
		return next;
	}

	public void setNext(Node n) {
		this.next = n;
	}
}
