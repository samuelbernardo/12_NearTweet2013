package com.example.neartweetclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.example.neartweetclient.TweetMenu.ClientConnectorTask1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TweetDialog extends Activity {
	 public static final String SERVER_IP = "192.168.1.3";
	private Button retweetButton;
	private TextView reply;
	private ListView listview1;
	private ArrayList<String> conversas;
	private ArrayAdapter<String> listAdapter ;
	private String username;
	private Conversa talk;
	private int index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_dialog);
		Intent intent = getIntent();
		talk = (Conversa) intent.getSerializableExtra("talk");
		username= intent.getStringExtra("username");
		index= intent.getIntExtra("index", 0);
		
	    retweetButton = (Button) findViewById(R.id.retweetbutton);
		listview1 = (ListView) findViewById(R.id.listView1);
		conversas = talk.getAll();
		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, conversas); 
		listview1.setAdapter( listAdapter );
		
		
	retweetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	reply = (TextView) findViewById(R.id.retweet);
            	String message = reply.getText().toString();
            	conversas.add(message);
            	
            	Intent intent = getIntent();
            //	String user = intent.getStringExtra("username");
            	
            	
                if(!(message.equals(null)  || message.equals(""))){
                	reply.setText("");
                //tweet.setText(message);
                new ClientConnectorTask1().execute("@Reply-@"+index+"-@all-"+username+"-"+message);
                finish();
                }
                
			}
        });
		
		 
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_dialog, menu);
		return true;
	}
	class ClientConnectorTask1 extends AsyncTask<String, Void, Integer> {
		
	    
	    private PrintWriter printwriter;
	    Socket  client;


	    protected Integer doInBackground(String...strings) {
	     // connect to the server and send the message
	            try {
	            	
	            		client = new Socket(SERVER_IP, 4444);
	                    printwriter = new PrintWriter(client.getOutputStream(),true);
	                    printwriter.write(strings[0]);
	                    printwriter.flush();
	                    printwriter.close();
	                    client.close();
	                    
	            } catch (UnknownHostException e) {
	                    e.printStackTrace();
	            } catch (IOException e) {
	                    e.printStackTrace();
	            }
	            return 0;
	    }

	    protected void onPostExecute(Long result) {
	            return;
	    }
	}
}
