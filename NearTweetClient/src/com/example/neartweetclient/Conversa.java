package com.example.neartweetclient;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversa implements Serializable{

	public ArrayList<String> conversa;
	
	Conversa(String firstTweet){
		conversa = new ArrayList<String>();
		conversa.add(firstTweet);	
	}
	
	public int getSize(){
		if (conversa != null)
             return conversa.size();
		return 0;		
	}
	
	public void addReply(String reply){
		if(reply != null)
			conversa.add(reply);
	}
	public ArrayList<String> getAll(){
            return conversa;
	}
}
