package com.example.neartweetclient;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	 public static final String SERVER_IP = "192.168.1.3";
    private Button button;
    private String message;
    private EditText textField;
   

 
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		  button = (Button) findViewById(R.id.login);
		  textField = (EditText) findViewById(R.id.user);
		          //Button press event listener
          button.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                          message = textField.getText().toString();
 
						try {
						new ClientConnectorTask1().execute("@Login-"+message);
                        Intent activityIntent = new Intent(MainActivity.this, TweetMenu.class);
	                    Bundle newActivityInfo = new Bundle();
	                    newActivityInfo.putString("username", message);// putDouble, putString, etc.
	                    activityIntent.putExtras(newActivityInfo);
	                    
	                    startActivity(activityIntent);
	                    
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                  }  
                         
          });
        
        
        
		
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
