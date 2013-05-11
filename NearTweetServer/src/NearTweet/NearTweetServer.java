package NearTweet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Vector;

//Classe do servidor
public class NearTweetServer {

  static Vector<Socket> ClientSockets;
  static Vector<String> UserNames;
  static Vector<Client> Clients;
  
  NearTweetServer() throws Exception{
	  ServerSocket soc = new ServerSocket(4444);
	  System.out.println("Server started. Listening to the port 4444");
	  ClientSockets = new Vector<Socket>();
	  UserNames = new Vector<String>();
	  Clients = new Vector<Client>();
	 
	
	  while(true){
		  try{
			 
		  Socket socC= soc.accept();
		  System.out.println(socC.getSendBufferSize()+"\n");
		  
	
		  InputStreamReader din = new InputStreamReader(socC.getInputStream());
		BufferedReader bufr = new BufferedReader(din);
		PrintWriter dout = new PrintWriter(socC.getOutputStream(), true);

			String message = bufr.readLine();
			
			//if(dout.checkError())System.out.println("Stream error!\n");
			
			//regista cliente
			if(message.contains("@Login")){
		    Client client = new Client();
		    String[] b = message.split("-");
			ClientSockets.add(socC);
			client.setUsername(b[1]);
			UserNames.add(b[1]);
			Clients.add(client);
			System.out.println("User Logged In :" + client.Username);
			}
			
			if(message.contains("@Tweet")){
				
				String[] s = message.split("-");
				System.out.println("Tweet received from user "+s[1]+":"+s[2]);
				int size=Clients.size();
				System.out.println("Clients size="+size);
				for(int i=0; i<size;i++){
				Client a = Clients.remove(i);
				System.out.println("Clients="+a.Username);
				a.setNewTweet(s[2]);
				Clients.add(a);
				}
			}
			if(message.contains("@Reply")){
				//reply, index, for who, username, message
				String[] s = message.split("-");
				System.out.println("Reply received from user "+s[3]+" to "+s[2]+":"+s[4]);
				int size=Clients.size();
				System.out.println("Clients size="+size);
					if(s[2].equals("@all")){
					for(int i=0; i<size;i++){
					Client a = Clients.remove(i);
					System.out.println("Clients="+a.Username);
					String[] str2 = s[1].split("@");
					System.out.println(str2[0]+"      "+str2[1]);
					a.setNewReply(s[4], str2[1]);
					Clients.add(a);
					}
				}
			}
			
			if(message.contains("@act")){
			String[] s = message.split("-");
			int aux=0;

			System.out.println(s[0]+" "+s[1]+"\n");
			if((aux = UserNames.indexOf(s[1]))==-1){
				socC.close();
				System.out.println("SISTEMA ABAIXO!");
				System.exit(-1);
			}
			
			Client c = Clients.remove(aux);
			if(c.refresh().equals("yes")){
				String tweetmsg = c.getNewTweet();
				System.out.println("Envia Tweet:"+tweetmsg+".\n");
				//dout.println(c.getNewTweet());
				dout.println("@Tweet@"+tweetmsg);
				
				if(dout.checkError())System.out.println("Stream error!\n");
				
			}else if(c.hasNewReply()){
					String newReply = c.getNewReply();
					String[] str = newReply.split("index:");
					System.out.println("Send : @reply-@"+c.Username+"-"+str[1]+"-"+str[0]);
					dout.write("@reply-@"+c.Username+"-"+str[1]+"-"+str[0]);
					dout.flush();
					
					if(dout.checkError())System.out.println("Stream error!\n");
					
					
				}else{
					dout.write("no tweet!");
					System.out.println("No Tweet.\n");
					dout.flush();
				}
			dout.close();
			Clients.add(c);
			}
			socC.close();
			message="";

		  }catch(IOException e){	  
			  System.out.println("IOException!");
			    System.exit(-1);
		  }catch(IllegalBlockingModeException e){
			  e.printStackTrace();
			  System.out.println("Channel error!");
		  }catch(Exception e){	  
			  e.printStackTrace();
			  System.out.println("Client not accepted!");
			    //System.exit(-1);
		  }
		  
	
	  }
	  

}

  //MAIN
  public static void main(String args[]) throws Exception{
	  try{
	  NearTweetServer nt = new NearTweetServer();
	  
	  }catch(Exception e){
		  }
	  }
  }
	  
  