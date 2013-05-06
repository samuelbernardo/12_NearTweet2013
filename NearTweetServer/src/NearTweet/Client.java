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
	 public String actualizar = "no";
	 public ArrayList<String> tweets = new ArrayList<String>();
	 public ArrayList<String> history = new ArrayList<String>();
Message msg=null;
	  
	  Vector<String> conversa = new Vector<String>();
	
	  public String getMessage(){
		  return this.message;
	  }
	  public void setUsername(String username){
		  this.Username=username;
	  }
	  
  
	  public String refresh(){
		  return actualizar;
	  }

	  public String getNewTweet(){
		  String message="";
		  for(int i=0; i<tweets.size(); i++){
			  if(!history.get(i).equals("no")){
				  message = tweets.get(i);
				  history.add(i, "no");
				  return message;
			  }
		  }
		  actualizar="no";
		return message;
	  }
	public void setNewTweet(String substring) {
		// TODO Auto-generated method stub
		tweets.add(substring);
		history.add("yes");
		System.out.println("Actualiza flag\n");
		actualizar="yes";
		
	}
	 
	  
  }
