package pt.utl.ist.cmov.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDevice;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.Channel;
import pt.utl.ist.cmov.wifidirect.service.SimWifiP2pService;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleChatActivity extends Activity implements PeerListListener {

    public static final String TAG = "simplechat";

    private SimWifiP2pManager mManager = null;
    private Channel mChannel = null;
    private Messenger mService = null;
	boolean mBound = false;
	
	private ServerSocket listenSocket = null;
	private ReceiveCommTask comm = null;
	Socket s = null;
	TextView input;
	TextView output;

	public SimWifiP2pManager getManager() {
		return mManager;
	}
	
	public Channel getChannel() {
		return mChannel;
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		findViewById(R.id.button2).setOnClickListener(startListener);
		findViewById(R.id.button1).setOnClickListener(sendListener);
		findViewById(R.id.button1).setEnabled(false);
		
		input = (TextView) findViewById(R.id.editText1);
		input.setHint("introduza IP");
		output = (TextView) findViewById(R.id.editText2);
		
		// Controls for the WDSim API
		Button start = (Button)findViewById(R.id.serviceButton);
		start.setOnClickListener(wifiStartListener);
		Button cancel = (Button)findViewById(R.id.cancelButton);
		cancel.setOnClickListener(wifiStopListener);		
		Button ping = (Button)findViewById(R.id.pingButton);
		ping.setOnClickListener(wifiPingListener);

		// Register intent filters for the WDSim API
		IntentFilter filter = new IntentFilter();
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver(this);
		registerReceiver(receiver, filter);

		new IncommingCommTask().execute();
	}

	private OnClickListener startListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			findViewById(R.id.button2).setVisibility(View.INVISIBLE);
			new OutgoingCommTask().execute(input.getText().toString());
		}
	};

	private OnClickListener sendListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			findViewById(R.id.button1).setEnabled(false);
			try {
				s.getOutputStream().write( (input.getText().toString()+"\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			input.setText("");
			findViewById(R.id.button1).setEnabled(true);
		}
	};

	private OnClickListener wifiStartListener = new OnClickListener() {
        public void onClick(View v){
        	Intent intent = new Intent(v.getContext(), SimWifiP2pService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
	};
   
	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			mManager = new SimWifiP2pManager(mService);
			mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService = null;
			mManager = null;
			mChannel = null;
			mBound = false;
		}
	};
	
	private OnClickListener wifiStopListener = new OnClickListener() {
        public void onClick(View v){
            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }
        }
	};

	private OnClickListener wifiPingListener = new OnClickListener() {
        public void onClick(View v){
        	if (mBound) {
        		mManager.discoverPeers(mChannel, new SimWifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), 
                        		"Discovery Initiated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(getApplicationContext(),
                        		"Discovery Failed : " + reasonCode,
                        		Toast.LENGTH_SHORT).show();
                    }
                });
        	} else {
            	Toast.makeText(v.getContext(), "Service not bound",
            		Toast.LENGTH_SHORT).show();
            }
        }
	};

	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		StringBuilder peersStr = new StringBuilder();
		for (SimWifiP2pDevice device : peers.getDeviceList()) {
			String devstr = "" + device.deviceName + " (" + device.deviceAddress + ")\n";
			peersStr.append(devstr);
		}
		new AlertDialog.Builder(this)
	    .setTitle("Available Peers")
	    .setMessage(peersStr.toString())
	    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        }
	     })
	     .show();
	}

	public class IncommingCommTask extends AsyncTask<Void, Socket, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				listenSocket = new ServerSocket(
						Integer.parseInt(getString(R.string.port)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Socket sock = listenSocket.accept();
					if (s != null)
						sock.close();
					else {
						publishProgress( sock);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Socket... values) {
			s = values[0];
			comm = new ReceiveCommTask();
			comm.execute(s);
		}
	}

	public class OutgoingCommTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			output.setText("Connecting...");
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				s = new Socket(params[0],
						Integer.parseInt(getString(R.string.port)));
			} catch (UnknownHostException e) {
				return "Unknown Host:" + e.getMessage();
			} catch (IOException e) {
				return "IO error:" + e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				output.setText(result);
				findViewById(R.id.button2).setVisibility(View.VISIBLE);
			}
			else {
				comm = new ReceiveCommTask();
				comm.execute( s);
			}		
		}
	}

	public class ReceiveCommTask extends AsyncTask<Socket, String, Void> {
		Socket s;
		
		@Override
		protected Void doInBackground(Socket... params) {
			BufferedReader sockIn;
			String st;
			
			s = params[0];
			try {
				sockIn = new BufferedReader(new InputStreamReader(s.getInputStream()));

				while ((st = sockIn.readLine()) != null) {
					publishProgress(st);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			output.setText("");
			findViewById(R.id.button1).setEnabled(true);
			findViewById(R.id.button2).setVisibility(View.INVISIBLE);
			input.setHint("");
			input.setText("");
			
		}

		@Override
		protected void onProgressUpdate(String... values) {
			output.append( values[0]+"\n");
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				s.close();
			}
			catch (Exception e) {
				Log.d("erro fecho", e.getMessage());
			}
			s = null;
			findViewById(R.id.button1).setEnabled(false);
			findViewById(R.id.button2).setVisibility(View.VISIBLE);
			input.setHint("introduza IP");
		}
	}
}
