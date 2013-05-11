package pt.utl.ist.cmov.wifidirect.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDevice;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.util.AsyncService;
import pt.utl.ist.cmov.wifidirect.util.Protocol;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class SimWifiP2pService extends AsyncService {

    private static final String TAG = "SimWifiP2pService";

    private static final int BASE = Protocol.BASE_SIM_WIFI_P2P_SERVICE;

    public static final int NEIGHBOR_UPDATE                        = BASE + 1;
    
    private static final int PORT = 9001;

    private volatile Thread mRunner;
    private SimWifiP2pDeviceList mDeviceList;

    public SimWifiP2pService() {
    	super();
    	mRunner = null;
    	mDeviceList = new SimWifiP2pDeviceList();
    }
    
	@Override
	public AsyncServiceInfo createHandler() {
		AsyncServiceInfo info = new AsyncServiceInfo();
		info.mHandler = new IncomingHandler(mDeviceList);
		info.mRestartFlags = 0;
		return info;
	}
	
	public Thread getRunner() {
		return mRunner;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this,"Service created ...", Toast.LENGTH_SHORT).show();
		
		// launch the worker thread listening on the WDSim control port
		mRunner = new SimWifiP2pWorkerThread(this,getHandler());
        mRunner.start();
        
        // send broadcast event: WIFI_P2P_STATE_CHANGED_ACTION
        Intent intent = new Intent();
        intent.setAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        intent.putExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE,
        		SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED);
        sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// kill the runner thread
		if (mRunner != null) {
			SimWifiP2pWorkerThread moribund = (SimWifiP2pWorkerThread) mRunner;
		    mRunner = null;
		    moribund.terminate();
		}

        // send broadcast event: WIFI_P2P_STATE_CHANGED_ACTION
        Intent intent = new Intent();
        intent.setAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        intent.putExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE,
        		SimWifiP2pBroadcast.WIFI_P2P_STATE_DISABLED);
        sendBroadcast(intent);
	}
    
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
    	SimWifiP2pDeviceList mDeviceList;
    	
    	public IncomingHandler(SimWifiP2pDeviceList devicelist) {
    		assert devicelist != null;
    		mDeviceList = devicelist;
    	}
    	
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SimWifiP2pService.NEIGHBOR_UPDATE:
                	
                	// parse the neighbors list sent by the WDSim console
                	boolean malformed = false;
                	ArrayList<SimWifiP2pDevice> dlist = 
                			new ArrayList<SimWifiP2pDevice>();
                	String [] neighbors = ((String) msg.obj).split("@");
                	for (String n : neighbors) {
                		try {
                			dlist.add(new SimWifiP2pDevice(n));
                		} catch (IllegalArgumentException e) {
                			malformed = true;
                			break;
                		} finally {
                		}
                	}
                	
                	if (!malformed) {
                		mDeviceList.clear();
                		mDeviceList.addList(dlist);

	                    // send broadcast event: WIFI_P2P_PEERS_CHANGED_ACTION
	                    Intent intent = new Intent();
	                    intent.setAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
	                    intent.putExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE,
	                    		SimWifiP2pBroadcast.WIFI_P2P_STATE_DISABLED);
	                    sendBroadcast(intent);
                	}
                	break;

                case SimWifiP2pManager.REQUEST_PEERS:
                {
                	assert msg.replyTo != null;

                	Message replymsg = Message.obtain(null, 
            				SimWifiP2pManager.RESPONSE_PEERS, 0, msg.arg2, mDeviceList);
                	try {
                        msg.replyTo.send(replymsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case SimWifiP2pManager.DISCOVER_PEERS:
                {
                	assert msg.replyTo != null;

                	Message replymsg = Message.obtain(null, 
            				SimWifiP2pManager.DISCOVER_PEERS_SUCCEEDED, 0, msg.arg2);
                	try {
                        msg.replyTo.send(replymsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    // send broadcast event: WIFI_P2P_PEERS_CHANGED_ACTION
                    Intent intent = new Intent();
                    intent.setAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
                    intent.putExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE,
                    		SimWifiP2pBroadcast.WIFI_P2P_STATE_DISABLED);
                    sendBroadcast(intent);
                    break;
                }

                default:
                    super.handleMessage(msg);
            }
        }
    }

	/*
	 * service thread: receives neighborhood updates from the wifi sim console
	 */

    private class SimWifiP2pWorkerThread extends Thread {

        private Handler mHandler;
        private SimWifiP2pService mService;
        ServerSocket serverSocket;
    	
        public SimWifiP2pWorkerThread(SimWifiP2pService service, Handler handler) {
        	mHandler = handler;
        	mService = service;
        	serverSocket = null;
        }

        public void terminate() {
        	if (serverSocket != null) {
        		try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	this.interrupt();
        }

    	@Override
    	public void run() {
    		super.run();
    		
            Socket clientSocket;
            InputStreamReader inputStreamReader;
            BufferedReader bufferedReader;
            String message;
            
            int port = SimWifiP2pService.PORT;

            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                Log.e(TAG, "Could not listen on port: " + port);
                return;
            }
     
            System.out.println("Server started. Listening to the port " + port);
            while (Thread.currentThread() == mService.getRunner()) {
                try {
                    clientSocket = serverSocket.accept();
                } catch (Exception ex) {
                    Log.d(TAG,"Interrupted in the service thread.");
                    return;
                }
                try {
                    inputStreamReader = new InputStreamReader(
                    		clientSocket.getInputStream());
                    bufferedReader = new BufferedReader(inputStreamReader);
                    message = bufferedReader.readLine();
                    inputStreamReader.close();
                    clientSocket.close();

                    // notify the upper layers
            		Message msg = mHandler.obtainMessage(
            				SimWifiP2pService.NEIGHBOR_UPDATE, message);
            		mHandler.sendMessage(msg);

                } catch (IOException e) {
                	Log.d(TAG,"Exception reading from client.");
                }
            }
    	}
    }
}
