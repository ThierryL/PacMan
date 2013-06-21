public class Test{


	public static void main(String[] args){
		ListAddr list = new ListAddr();

		Node node1 = list.getServer("paris");
		System.out.println(node1.getName());	
		System.out.println(node1.getIpAddr());	
		System.out.println(node1.getPort());	
		System.out.println(node1.getPortClient());	

		Node node2 = list.getNextServer("paris");
		System.out.println(node2.getName());	
		System.out.println(node2.getIpAddr());	
		System.out.println(node2.getPort());	
		System.out.println(node2.getPortClient());	

		System.out.println("************");	
		System.out.println("************");	
		System.out.println("************");	
		Node node3 = list.getServer("lyon");
		System.out.println(node3.getName());	
		System.out.println(node3.getIpAddr());	
		System.out.println(node3.getPort());	
		System.out.println(node3.getPortClient());	

		Node node4 = list.getNextServer("lyon");
		System.out.println(node4.getName());	
		System.out.println(node4.getIpAddr());	
		System.out.println(node4.getPort());	
		System.out.println(node4.getPortClient());	

		System.out.println("************");	
		System.out.println("************");	
		System.out.println("************");	
		Node node5 = list.getServer("santiago");
		System.out.println(node5.getName());	
		System.out.println(node5.getIpAddr());	
		System.out.println(node5.getPort());	
		System.out.println(node5.getPortClient());	

		Node node6 = list.getNextServer("santiago");
		System.out.println(node6.getName());	
		System.out.println(node6.getIpAddr());	
		System.out.println(node6.getPort());	
		System.out.println(node6.getPortClient());	
	}
}
