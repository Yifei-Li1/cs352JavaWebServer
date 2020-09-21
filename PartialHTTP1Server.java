import java.io.*;
import java.net.*;
public class serverThread extends Tread{
	Socket socket;
	serverThread(Socket socket){
		this.socket = socket;
	}
	public void run{
		try{

		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
public class PartialHTTP1Server{
	public static void main(String[] args) {
		int portNum = args[0];
		ServerSocket serverSocket = new ServerSocket(portNum);
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			serverThread thread = new serverThread(connectionSocket);
	 	thread.start();
	 	//thread.join();
		}
	}
}