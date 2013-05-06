package NearTweet;

import java.io.PrintWriter;

public class Message implements Runnable{
	String msgType;
	String content;
	PrintWriter out;
	
	Message(PrintWriter out, String msg){
		this.out=out;
		this.content=msg;
		
	}
	
	public String getMsgTyoe(){
		return msgType;
	}
	public String getContent(){
		return content;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Envia Tweet:"+content+".\n");
		out.write(content);
		System.out.println(content+"\n");
		out.flush();
	}

}
