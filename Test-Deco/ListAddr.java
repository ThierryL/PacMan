import java.io.*;

public class ListAddr {

	private Node first = null;
	private String file = "./listAddr.txt";
	private int size = 0;
	
	public ListAddr() {
		Node current = new Node();
		try{
			InputStream ips=new FileInputStream(file); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){
				if (first == null) {
					first = new Node(line);
					current = first;
				}
				else {
					current.setNext(new Node(line));
					current = current.getNext();
				}
				size++;
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		current.setNext(first);
	}

	public Node getServer(String name) {
		Node tmp = first;
		for(int i = 0; i<size; i++) {
			if (name.equals(tmp.getName())) return tmp;
			tmp = tmp.getNext();
		}
		return null;
	}

	public Node getNextServer(String name) {
		Node tmp = first;
		for(int i = 0; i<size; i++) {
			if (name.equals(tmp.getName())) return tmp.getNext();
			tmp = tmp.getNext();
		}
		return null;
	}
}
