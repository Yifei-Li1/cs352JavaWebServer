import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.lang.Object.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
class serverThread extends Thread{
	Socket socket;
	String sentence = "";
	String res;

	serverThread(Socket socket){
		this.socket = socket;
	}
	public void run(){
		try{

			Scanner inFromClient = new Scanner(socket.getInputStream()); 
			PrintStream outToClient = new PrintStream(socket.getOutputStream());
			System.out.println("I'm ready");
			ArrayList<String> in = new ArrayList<String>();

			while(inFromClient.hasNextLine()){
				String temp = inFromClient.nextLine();
				if(temp == null || temp.isEmpty()){ //if the line is empty
       				break;  //exit the loop
   			 	}
				in.add(temp);
			}

			for (int i = 0; i < in.size();i++) 
	      { 		      
	        if(i == 0){sentence = (in.get(i))+"\n";
	         }
	        else{

	        	sentence = sentence +(in.get(i))+"\n";
	        }		
	      }   
			System.out.println(sentence);
			String command;
			String resource;
			String http;
			String ifModifiedSince;
			long ifModified = 0;
			int sp1 = sentence.indexOf(' ');
			int sp2 = sentence.indexOf(' ', sp1+1);
			int sp3 = sentence.indexOf('\n');
			int sp4 = sentence.indexOf(' ',sp3+1);
			//System.out.println("sp4="+sp4);
			if(sp1 == -1 || sp2 == -1){						//没有空格
				res = "HTTP/1.0 400 Bad Request\n";
			}else{											//检查http版本
				command = sentence.substring(0,sp1);
				resource = sentence.substring(sp1+1,sp2);
				http = sentence.substring(sp2+1,sp3);
				if(sp4 == -1){ifModifiedSince = null;}
				else{
				ifModifiedSince = sentence.substring(sp4+1);
				}
				System.out.println("ifModifiedSince:"+ ifModifiedSince+"\n");
				System.out.println(command +"\n"+resource+"\n"+ http+"\n"+ifModifiedSince+"\n");
				res = sentence+"\n";
				if(http.substring(0,5).equals("HTTP/")){						
					double httpver = Double.parseDouble(http.substring(5));
					if (httpver != 1.0) {					//版本不支持
						res = "HTTP/1.0 505 HTTP Version Not Supported\n";
					}
				}else{									//http这4个字母不对
					res = "HTTP/1.0 400 Bad Request\n";
				}
				if(command.equals("GET")||command.equals("POST")){    //如果command是get\post
					String dir = System.getProperty("user.dir");
					File f = new File(dir+resource);					//find  file 
					boolean exist = f.exists();							//check  if file  exist
					boolean canWrite = f.canWrite();
					long date = f.lastModified();
					Calendar cal = Calendar.getInstance();				
					cal.setTimeInMillis(date);							//convert Last-Modified  long to calendar
					String strdate = null;	//convert calendar  to string
					SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
					if (cal != null) {
						strdate = sdf2.format(cal.getTime());
					}
					if(ifModifiedSince!= null){							//check If-Modified-Since, convert  from  string to calendar
						Calendar modifiedSince = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z",Locale.ENGLISH);
						try{
							modifiedSince.setTime(sdf.parse(ifModifiedSince));
						}
						catch(ParseException e){
							
						}
					ifModified = modifiedSince.getTimeInMillis();		//convert If-Modified-Since from  calendar to  long,  easy  to compare
					}
					long length = f.length();
					Path path = f.toPath();
					String mimeType = "";								//find  mimeType
					try{
			    	mimeType = Files.probeContentType(path);
					}
					catch (IOException e){
						e.printStackTrace();
					}
					if(mimeType == null){
						mimeType = "application/octet-stream";
					}
					if(!exist){											//file doesn't exist
						res ="HTTP/1.0 404 Not Found";
					}
					else if(!canWrite){									//file can't be writen
						res = "HTTP/1.0 403 Forbidden";

					}
					else if(ifModified!= 0) {							//not modified  after  If-Modified-Since 
						if(ifModified<date){
						res = "HTTP/1.0 304 Not Modified";}
						else{
							res =  "HTTP/1.0 200 OK\n"+"Content-Type:"+mimeType+"\n"+"Content-Length:"+length+
								"\n"+"Last-Modified:"+strdate+"\n"+"Content-Encoding: identity";
						}
					}



				}
				else if(command.equals("HEAD")){



				}
				else if(command.equals("DELETE")||command.equals("PUT")||command.equals("LINK")||command.equals("UNLINK")){	//不支持的command
					res = "HTTP/1.0 501 Not Implemented\n";
				}
				else{   //错误command
					res = "HTTP/1.0 400 Bad Request\n";
				}
			}
			
			
			

			outToClient.println(res);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
public class PartialHTTP1Server{
	private static final int MAXTHREAD = 50;
	public static void main(String[] args) throws IOException{
		int portNum = Integer.parseInt(args[0]);
		
		int threadCount = 0;
		ServerSocket serverSocket = new ServerSocket(portNum);
		
		Socket connectionSocket = serverSocket.accept();
		PrintStream outToClient = new PrintStream(connectionSocket.getOutputStream());
		//connectionSocket.setSoTimeout(5000);
		//outToClient.println("HTTP/1.0 408 Request Timeout");
		serverThread thread = new serverThread(connectionSocket);
		thread.start();
		try 
{
    thread.join();
} 
catch(InterruptedException e)
{
     // this part is executed when an exception (in this example InterruptedException) occurs
}
		
		/*
		while(threadCount <= MAXTHREAD){
			Socket connectionSocket = welcomeSocket.accept();
			serverThread thread = new serverThread(connectionSocket);
	 	thread.start();
	 	threadCount++;
	 	//thread.join();
		}
		*/
	}
}