package com.example.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.os.AsyncTask;

import android.widget.TextView;

public class ClientReceiverTask extends AsyncTask<String , Void, String> {
	private String message;
	private Socket client;
	private PrintWriter printwriter;
	private InputStreamReader in;
	private BufferedReader buf;
	private String flag;
	
	public String getMessage(){
		return message;
	}

	
	protected void onPostExecute(String str) {
		
        return;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
		
		client = new Socket("192.168.1.3", 4444);
		       
        printwriter = new PrintWriter(client.getOutputStream(),true);
        printwriter.write(params[0]);
        printwriter.flush();
        in = new InputStreamReader(client.getInputStream());
        char[] buffer = null;
        while(in.read(buffer)!=-1 );
        message = buffer.toString();
		} catch (UnknownHostException e) {
            e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
		}
		
		return message;
	}
}