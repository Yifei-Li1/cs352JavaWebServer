import java.io.*;
import java.net.*;
import java.util.*;
public class testClient{
public static void main(String[] args) throws IOException{
	String SERVER_IP = "127.0.0.1";
	int SERVER_PORT = 6780; 
	String sentence = "";
	String result = "";
	Socket clientSocket = new Socket(SERVER_IP, SERVER_PORT);
	System.out.println("Client input:");
 	 //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
 	Scanner s = new Scanner(System.in);
 	Scanner inFromServer = new Scanner(clientSocket.getInputStream()); 
	ArrayList<String> in = new ArrayList<String>();
	while (s.hasNextLine()){ //no need for "== true"
    String read = s.nextLine();
    if(read == null || read.isEmpty()){ //if the line is empty
        break;  //exit the loop
    }
    in.add(read);
}
     for (int i = 0; i < in.size();i++) 
	      { 		      
	        if(i == 0)sentence = (in.get(i))+"\n"; 
	        else{
	        	sentence = sentence +(in.get(i))+"\n";
	        }		
	      }   
	 DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	 System.out.println(sentence);
	 outToServer.writeBytes(sentence+"\n");
	 
	 //System.out.println("I'm here");
	 //sentence = inFromUser.readLine();
	 ArrayList<String> out = new ArrayList<String>();
			while(inFromServer.hasNextLine()){
				String temp = inFromServer.nextLine();
				if(temp == null || temp.isEmpty()){ //if the line is empty
       				break;  //exit the loop
   			 	}
				out.add(temp);
			}
			 for (int i = 0; i < out.size();i++) 
	      	{ 		      
	        if(i == 0)result = (out.get(i))+"\n"; 
	        else{
	        	result = result +(out.get(i))+"\n";
	        }		
	    	  }
	 System.out.println("Server response:" + result);
}
}