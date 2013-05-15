import java.io.*;
import java.net.*;

class TestServer
{
   public static void main(String argv[]) throws Exception
      {
         String clientSentence;
         String capitalizedSentence;
         ServerSocket welcomeSocket = new ServerSocket(6789);


	

	InetAddress inetAddress = InetAddress.getByName("192.168.0.105");
	System.out.println(inetAddress.getHostAddress());

	InetSocketAddress address = new InetSocketAddress("192.168.0.105", 5555);
	System.out.println(address.getAddress().getHostAddress());

         while(true)
         {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);
         }
      }
}
