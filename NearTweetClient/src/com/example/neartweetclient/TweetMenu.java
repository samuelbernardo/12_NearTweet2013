package com.example.neartweetclient;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class TweetMenu extends Activity implements OnTouchListener{
	
	 public static final String SERVER_IP = "192.168.1.2";
	 float downXValue;
	 private Button button;
	 private String message="no";
	 private Timer time;
	 private String mensagem = "null";
	// private Date data = new Date();
	 private ArrayList<String> tweetsList = new ArrayList<String>();
	 private ArrayList<Conversa> talks = new ArrayList<Conversa>();
	 private ListView mainListView;
	 private ArrayAdapter<String> listAdapter ;
	 private String username ;
	 private static final int CAMERA_REQUEST = 1888;
	private Config Config = null; 
	 private ImageView imageView;
	 private Button photoButton;
	 private Bitmap Foto=null;
	 

	 

	 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweet_menu);
			
		LinearLayout layMain = (LinearLayout) findViewById(R.id.layout_main);
        layMain.setOnTouchListener((OnTouchListener) this); 
		TextView et = (TextView) findViewById(R.id.usermenu);
		
		this.imageView = (ImageView)this.findViewById(R.id.imageView1);
		photoButton = (Button) this.findViewById(R.id.picture);
	    photoButton.setOnClickListener(new View.OnClickListener() {
	    	  @Override
	            public void onClick(View v) {
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	            }
	    });
	    
		button = (Button) findViewById(R.id.button1);
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		time= new Timer();
		mainListView = (ListView) findViewById( R.id.listview );    
		et.setText(username);

		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, tweetsList); 
		mainListView.setAdapter( listAdapter );
		mainListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		

		mainListView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> view, View view1, int pos, long arg3) {
		        Intent in = new Intent(TweetMenu.this, TweetDialog.class);
		        Bundle b = new Bundle();
		       
				Conversa getSelectedItemOfList = (Conversa) talks.get(pos);
				b.putInt("index", pos);
				b.putSerializable("talk", getSelectedItemOfList);
				b.putString("username", username);
		        in.putExtras(b);
		        startActivity(in);
		    }
		});
		
		//Temporizador para actualizar o cliente
			time.schedule(new TimerTask() {
				TextView tweet = (TextView) findViewById(R.id.tweet);	
				TextView et = (TextView) findViewById(R.id.usermenu);
				@Override
				public void run() {
					// TODO Auto-generated method stub	
				new ClientReceiverTask1().execute(et, tweet);
			
			if(mensagem.contains("@reply")){
				String[] str = mensagem.split("-");
				// @reply-@username-index-msg
				Conversa c = talks.remove(Integer.parseInt(str[2]));
				c.addReply(str[3]);
				talks.add(Integer.parseInt(str[2]), c);
				mensagem="null";
				
			}else{
				if(mensagem.contains("@Tweet@")){
					String[] str = mensagem.split("@Tweet@");
				
				if(mensagem.contains("@image@")){
				String[] MsgEimage = str[1].split("@image@");
				Conversa conv = new Conversa(MsgEimage[0]);
				tweetsList.add(MsgEimage[0]); 
				talks.add(conv);
				ImageView i = (ImageView) findViewById(R.id.imagem_recebida);
			
				Bitmap bitmap = BitmapFactory.decodeByteArray(MsgEimage[1].getBytes() , 0, MsgEimage[1].length());
				//Bitmap back = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Config);
				
				i.setImageBitmap(bitmap);
				}else{
				tweetsList.add(str[1]); 
				Conversa conv = new Conversa(str[1]);
				talks.add(conv);
				}
				
				
				mensagem="null";
				}
				}
				}}, 3000,1000);
			
		
		// botao responsavel para enviar tweet para servidor
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(Foto !=null){
            	Intent intent = getIntent();
            	String user = intent.getStringExtra("username");
            	EditText edt = (EditText) findViewById(R.id.editText1);
            	message = edt.getText().toString();
            	
            	//Tentar passar imagem para array
            	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            	 Foto.compress(CompressFormat.PNG, 0, byteArrayOutputStream);
            	 byte[] b = byteArrayOutputStream.toByteArray();
    
                edt.setText("");
                //Enviar Tweet com imagem
                new ClientConnectorTask1().execute("@Tweet-"+user+"-"+message+"@image@"+b.toString());
                Foto=null;
               
            	}else{//Enviar tweet sem imagem
                	Intent intent = getIntent();
                	String user = intent.getStringExtra("username");
                	EditText edt = (EditText) findViewById(R.id.editText1);
                	message = edt.getText().toString();
                	edt.setText("");
                    new ClientConnectorTask1().execute("@Tweet-"+user+"-"+message);
            	}
			}
        });
	
	}
	
		// botao responsavel para enviar tweet para servidor


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
            Foto=photo;
            Config = Foto.getConfig();
        }  
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

 class ClientReceiverTask1 extends AsyncTask<TextView , Void, String> {
	public String msg = "null";
	private Socket client=null;
	private PrintWriter printwriter;
	
	private BufferedReader buf;
	
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
			
		client = new Socket(SERVER_IP,4444);
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