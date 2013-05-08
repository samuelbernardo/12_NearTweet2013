package com.example.neartweetclient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class TweetMenu extends Activity implements OnTouchListener{
	
	 float downXValue;
	 private Button button;
	 private Button buttonFacebook;
	 private long leituras=0;	 
	 private String message="no";
	 private InputStreamReader in;
	 private BufferedReader buf;
	 private Timer time;
	 private String mensagem = "null";
	 private Date data = new Date();
	 private ArrayList<String> tweetsList = new ArrayList<String>();
	 private ListView mainListView;
	 private ArrayAdapter<String> listAdapter ;

	 

	 

	 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweet_menu);
	
	
		
		LinearLayout layMain = (LinearLayout) findViewById(R.id.layout_main);
        layMain.setOnTouchListener((OnTouchListener) this); 
		TextView et = (TextView) findViewById(R.id.usermenu);
		TextView tweet = (TextView) findViewById(R.id.tweet);	
		button = (Button) findViewById(R.id.button1);
		buttonFacebook = (Button) findViewById(R.id.facebook);
		Intent intent = getIntent();
		time= new Timer();
		String username = intent.getStringExtra("username");
		mainListView = (ListView) findViewById( R.id.listview );    
		et.setText(username);
	//	rowTextView
		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, tweetsList); 
		mainListView.setAdapter( listAdapter );
		mainListView.setOnTouchListener((OnTouchListener) this);
		//Temporizador para actualizar o cliente
			time.schedule(new TimerTask() {
				TextView tweet = (TextView) findViewById(R.id.tweet);	
				TextView et = (TextView) findViewById(R.id.usermenu);
				@Override
				public void run() {
					// TODO Auto-generated method stub	
				new ClientReceiverTask1().execute(et, tweet);
			
				//guardar data e tweet num array
			//int day =data.getDay();
				if(!(mensagem.equals("null") || mensagem.equals("no tweet!"))){
				tweetsList.add(mensagem); 
				mensagem="null";
				}
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
	
	
	
		// botao responsavel para enviar tweet para servidor
      buttonFacebook.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
        	  Intent shareIntent = new Intent(Intent.ACTION_SEND);
        	  shareIntent.setType("text/plain");
        	  shareIntent.putExtra(Intent.EXTRA_TEXT, "URLyouWantToShare");
        	  startActivity(Intent.createChooser(shareIntent, "Share..."));
			}
      });
	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
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
            	
            		client = new Socket("192.168.1.3", 4444);
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
	public String msg = "null";
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
		client = new Socket("192.168.1.3",4444);
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