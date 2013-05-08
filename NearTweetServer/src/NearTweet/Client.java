package NearTweet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Client  {
	 public Socket ClientSocket;
	 public InputStreamReader din;
	 public OutputStreamWriter dout;
	 public BufferedReader bufr;
	 public BufferedWriter bufw;
	 public String Username;
	 public String message;
	 public String actualizar;
	 public ArrayList<String> tweets ;
	 public ArrayList<String> history ;
Message msg=null;
	  
	  Vector<String> conversa = new Vector<String>();
	
	  
	  Client(){
		  this.actualizar="no";
		this.tweets = new ArrayList<String>();
		this.history = new ArrayList<String>();
	  }
	  
	  public String getMessage(){
		  return this.message;
	  }
	  public void setUsername(String username){
		  this.Username=username;
	  }
	  
  
	  public String refresh(){
		  return this.actualizar;
	  }

	  public String getNewTweet(){
		 String msgTweet = null;
		 int i;
		 if((i = history.indexOf("yes"))!=-1){
			  msgTweet = tweets.get(i);
			  this.history.remove(i);
			  this.history.add(i, "no");
			  if(!history.contains("yes")){
				  System.out.println("Desactualiza flag");
				  this.actualizar="no";
				  }
			  
			  return msgTweet;
		 }
		return msgTweet;
	  }
	  
	public void setNewTweet(String substring) {
		// TODO Auto-generated method stub
		this.tweets.add(substring);
		this.history.add("yes");
		System.out.println("Actualiza flag");
		this.actualizar="yes";
		
	}
	 
	  
  }
