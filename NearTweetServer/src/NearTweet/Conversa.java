package NearTweet;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<String> conversa;
	public ArrayList<String> history;
	
	Conversa(String firstTweet){
		conversa = new ArrayList<String>();
		history = new ArrayList<String>();
		conversa.add(firstTweet);
		history.add("no");
	}
	
	public int getSize(){
		if (conversa != null)
             return conversa.size();
		return 0;		
	}
	
	public void addReply(String reply){
		if(reply != null){
			conversa.add(reply);
			history.add("yes");
		}
	}
	public ArrayList<String> getAll(){
            return conversa;
	}
}
