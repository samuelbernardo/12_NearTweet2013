package NearTweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//Classe do servidor
public class NearTweetServer {

  static Vector<Socket> ClientSockets;
  static Vector<String> UserNames;
  static Vector<Thread> Clients;
  
  NearTweetServer() throws Exception{
	  ServerSocket soc = new ServerSocket(4444);
	  System.out.println("Server started. Listening to the port 4444");
	  ClientSockets = new Vector<Socket>();
	  UserNames = new Vector<String>();
	  Clients = new Vector<Thread>();
	  
	  while(true){
		  try{
		  Socket socC= soc.accept();
		  Client client = new Client(socC);
			ClientSockets.add(socC);
			UserNames.add(client.getUsername());
			Clients.add(client);
			System.out.println("User Logged In :" + client.Username);
			
		  }catch(Exception e){	  
			  System.out.println("Client not accepted!");
			    System.exit(-1);
		  }
		  
		  
		  try{
			  
			 
		  }catch(Exception e){
			  System.out.println("Process");
			    System.exit(-1);
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
	  
  