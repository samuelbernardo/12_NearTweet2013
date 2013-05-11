package pt.utl.ist.cmov.simplechat;

import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

    private SimpleChatActivity activity;

    public SimWifiP2pBroadcastReceiver(SimpleChatActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        	// This action is triggered when the WDSim service changes state:
        	// - creating the service generates the WIFI_P2P_STATE_ENABLED event
        	// - destroying the service generates the WIFI_P2P_STATE_DISABLED event

            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
        		Toast.makeText(activity, "WiFi Direct enabled",
        				Toast.LENGTH_SHORT).show();
            } else {
        		Toast.makeText(activity, "WiFi Direct disabled",
        				Toast.LENGTH_SHORT).show();
            }
            Log.d(SimpleChatActivity.TAG, "P2P state changed - " + state);

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()

            SimWifiP2pManager manager = activity.getManager();
        	SimWifiP2pManager.Channel channel = activity.getChannel();
        	if (manager != null) {
                manager.requestPeers(channel, (PeerListListener) activity);
            }
            Log.d(SimpleChatActivity.TAG, "P2P peers updated");

        } else if (SimWifiP2pBroadcast.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

        	// Not currently implemented in the WDSim API

        } else if (SimWifiP2pBroadcast.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        	// Not currently implemented in the WDSim API
        }
    }
}
