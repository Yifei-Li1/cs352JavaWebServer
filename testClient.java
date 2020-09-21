import java.io.*;
import java.net.*;
private static final String SERVER_IP = "127.0.0.1";
private static final int SERVER_PORT = 6780; 
public static void main(String[] args) throws IOException{
	Socket clientSocket = new Socket(SERVER_IP, SERVER_PORT);
	 System.out.println("Client input:");
 	 BufferedReader inFromUser1 = new BufferedReader(new InputStreamReader(System.in));
	 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	 BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
}