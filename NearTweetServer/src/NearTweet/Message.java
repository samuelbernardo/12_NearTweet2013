package NearTweet;

public class Message {
	String msgType;
	String content;
	
	Message(String type, String cnt){
		msgType = type;
		content = cnt;
	}
	
	public String getMsgTyoe(){
		return msgType;
	}
	public String getContent(){
		return content;
	}

}
