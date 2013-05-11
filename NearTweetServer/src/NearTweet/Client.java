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
	 public ArrayList<Conversa> tweets ;
	 public ArrayList<String> history ;
Message msg=null;
	  
	  Vector<String> conversa = new Vector<String>();
	private String actualizarReply;
	
	  
	  Client(){
		  this.actualizar="no";
		this.tweets = new ArrayList<Conversa>();
		this.history = new ArrayList<String>();
		this.actualizarReply="no";
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
	  public boolean hasNewReply(){
		  int i;
		  for(i=0; i<tweets.size();i++){
			  Conversa c = tweets.get(i);
			  if(c.history.contains("yes")){
				  return true;
			  }
		  }
		  return false;
	  }

	  public String getNewTweet(){
		 String msgTweet = null;
		 int i;
		 if((i = history.indexOf("yes"))!=-1){
			 
			Conversa c = tweets.remove(i);
			 msgTweet =  c.conversa.get(0);
			  
			 tweets.add(i,c) ;
			  
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
		this.tweets.add(new Conversa(substring));
		this.history.add("yes");
		System.out.println("Actualiza flag");
		this.actualizar="yes";
		
	}

	public void setNewReply(String string, String string2) {
		// TODO Auto-generated method stub
		System.out.println("Adiciona novo reply ");
		Conversa conv = tweets.remove(  Integer.parseInt(string2) );
		conv.addReply(string);
		tweets.add(Integer.parseInt(string2), conv);

		
	}
	
	public String getNewReply(){
		int i, aux;
		for(i=0; i<tweets.size(); i++){
			Conversa c = tweets.remove(i);
			if((aux=c.history.indexOf("yes")) != -1){
				
				String reply = c.conversa.get(aux);
				c.history.remove(aux);
				c.history.add(aux, "no");
				tweets.add(i, c);
				return reply+"index:"+i;
			}
			tweets.add(i, c);
			
		}
		
		return null;
		
	}
	 
	  
  }
