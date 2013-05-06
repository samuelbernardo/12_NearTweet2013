package com.example.neartweetclient;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class TweetMenu extends Activity implements OnTouchListener{
	
	 float downXValue;
	 private Button button;
	 private long leituras=0;	 
	 private String message="no";
	 private InputStreamReader in;
	 private BufferedReader buf;
	 private Timer time;
	 String mensagem = "null";
	 private Date data = new Date();
	 

	 

	 

	 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweet_menu);
	
	
		
		LinearLayout layMain = (LinearLayout) findViewById(R.id.layout_main);
        layMain.setOnTouchListener((OnTouchListener) this); 
		TextView et = (TextView) findViewById(R.id.usermenu);
		TextView et1 = (TextView) findViewById(R.id.usermenu1);
		TextView tweet = (TextView) findViewById(R.id.tweet);	
		button = (Button) findViewById(R.id.button1);
		Intent intent = getIntent();
		time= new Timer();
		String username = intent.getStringExtra("username");
		
		et.setText(username);
		et1.setText(username);
		//tweet.setText("TWEET");
		
		
		//Temporizador para actualizar o cliente
			time.schedule(new TimerTask() {
				TextView tweet = (TextView) findViewById(R.id.tweet);	
				TextView et = (TextView) findViewById(R.id.usermenu);
				@Override
				public void run() {
					// TODO Auto-generated method stub	
				new ClientReceiverTask1().execute(et, tweet);
			
				//guardar data e tweet num array
				int day =data.getDay();
				//mensagem -> novo tweet;
			
				}}, 5000,5000);
			
		
		// botao responsavel para enviar tweet para servidor
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = getIntent();
            	String user = intent.getStringExtra("username");
            	EditText edt = (EditText) findViewById(R.id.editText1);
            	message = edt.getText().toString();
                edt.setText("");
                //tweet.setText(message);
                new ClientConnectorTask1().execute("@Tweet-"+user+"-"+message);
            
			}
        });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		  
		// Get the action that was done on this touch event
        switch (arg1.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                // store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();            

                // going backwards: pushing stuff to the right
                if (downXValue < currentX)
                {
                    // Get a reference to the ViewFlipper
                     ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
                     // Set the animation
                      vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                      // Flip!
                      vf.showPrevious();
                }

                // going forwards: pushing stuff to the left
                if (downXValue > currentX)
                {
                    // Get a reference to the ViewFlipper
                    ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
                     // Set the animation
                     vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                      // Flip!
                     vf.showNext();
                }
                break;
            }
        }

        // if you return false, these actions will not be recorded
        return true;
	}


class ClientConnectorTask1 extends AsyncTask<String, Void, Integer> {
	
    
    private PrintWriter printwriter;
    Socket  client;


    protected Integer doInBackground(String...strings) {
     // connect to the server and send the message
            try {
            	
            		client = new Socket("194.210.230.95", 4444);
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

 class ClientReceiverTask1 extends AsyncTask<TextView , Void, String> {
	public String msg = null;
	private Socket client=null;
	private PrintWriter printwriter;
	private InputStreamReader in;
	private BufferedReader buf;
	private String flag;
	 TextView user;
     TextView tweet;
	
	protected void onPostExecute(String str) {
		mensagem = str;
		
        return;
	}

	@Override
	protected String doInBackground(TextView... params) {
		// TODO Auto-generated method stub
		 user = params[0];
	     tweet = params[1];
		try{
		client = new Socket("194.210.230.95",4444);
		printwriter = new PrintWriter(client.getOutputStream(),true);
		buf = new BufferedReader( new InputStreamReader(client.getInputStream()));
		
        printwriter.println("@act-"+user.getText().toString());
        msg=buf.readLine();
        
        printwriter.close();
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        buf.close();
        client.close();
    
		} catch (UnknownHostException e) {
            e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
		}
	
		return msg;
	}
}
}