package NearTweet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

public class Client extends Thread {
	  Socket ClientSocket;
	  InputStreamReader din;
	  OutputStreamWriter dout;
	  BufferedReader bufr;
	  BufferedWriter bufw;
	  String Username;
Message msg;
	  
	  Vector<String> conversa = new Vector<String>();
	  
	  Client(Socket soc) throws Exception{
			ClientSocket = soc;		
			din = new InputStreamReader(ClientSocket.getInputStream());
			dout = new OutputStreamWriter(ClientSocket.getOutputStream());
			bufr = new BufferedReader(din);
			bufw = new BufferedWriter(dout);
			Username = bufr.readLine();
			
			

			start();
		}
	  
	  
	  
	  public String getUsername(){
		  return this.Username;
	  }
	  
	  public void run(){ 
		  try{
		  String str = bufr.readLine();
	  }catch(Exception e){
		  
	  }
	  };
	  
  }
