package pt.utl.ist.cmov.wifidirect;

public class SimWifiP2pBroadcast {
    /**
     * Broadcast intent action to indicate whether Wi-Fi p2p is enabled or disabled. An
     * extra {@link #EXTRA_WIFI_STATE} provides the state information as int.
     *
     * @see #EXTRA_WIFI_STATE
     */
    public static final String WIFI_P2P_STATE_CHANGED_ACTION =
        "pt.utl.ist.cmov.wifidirect.STATE_CHANGED";

    /**
     * The lookup key for an int that indicates whether Wi-Fi p2p is enabled or disabled.
     * Retrieve it with {@link android.content.Intent#getIntExtra(String,int)}.
     *
     * @see #WIFI_P2P_STATE_DISABLED
     * @see #WIFI_P2P_STATE_ENABLED
     */
    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";

    /**
     * Wi-Fi p2p is disabled.
     *
     * @see #WIFI_P2P_STATE_CHANGED_ACTION
     */
    public static final int WIFI_P2P_STATE_DISABLED = 1;

    /**
     * Wi-Fi p2p is enabled.
     *
     * @see #WIFI_P2P_STATE_CHANGED_ACTION
     */
    public static final int WIFI_P2P_STATE_ENABLED = 2;

    /**
     * Broadcast intent action indicating that the state of Wi-Fi p2p connectivity
     * has changed. One extra {@link #EXTRA_WIFI_P2P_INFO} provides the p2p connection info in
     * the form of a {@link WifiP2pInfo} object. Another extra {@link #EXTRA_NETWORK_INFO} provides
     * the network info in the form of a {@link android.net.NetworkInfo}.
     *
     * @see #EXTRA_WIFI_P2P_INFO
     * @see #EXTRA_NETWORK_INFO
     */
    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION =
        "pt.utl.ist.cmov.wifidirect.CONNECTION_STATE_CHANGE";

    /**
     * The lookup key for a {@link android.net.wifi.p2p.WifiP2pInfo} object
     * Retrieve with {@link android.content.Intent#getParcelableExtra(String)}.
     */
    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";

    /**
     * The lookup key for a {@link android.net.NetworkInfo} object associated with the
     * Wi-Fi network. Retrieve with
     * {@link android.content.Intent#getParcelableExtra(String)}.
     */
    public static final String EXTRA_NETWORK_INFO = "networkInfo";

    /**
     * The lookup key for a {@link android.net.LinkProperties} object associated with the
     * network. Retrieve with
     * {@link android.content.Intent#getParcelableExtra(String)}.
     * @hide
     */
    public static final String EXTRA_LINK_PROPERTIES = "linkProperties";

    /**
     * The lookup key for a {@link android.net.LinkCapabilities} object associated with the
     * network. Retrieve with
     * {@link android.content.Intent#getParcelableExtra(String)}.
     * @hide
     */
    public static final String EXTRA_LINK_CAPABILITIES = "linkCapabilities";

    /**
     * Broadcast intent action indicating that the available peer list has changed. Fetch
     * the changed list of peers with {@link #requestPeers}
     */
    public static final String WIFI_P2P_PEERS_CHANGED_ACTION =
        "pt.utl.ist.cmov.wifidirect.PEERS_CHANGED";

    /**
     * Broadcast intent action indicating that peer discovery has either started or stopped.
     * One extra {@link #EXTRA_DISCOVERY_STATE} indicates whether discovery has started
     * or stopped.
     *
     * Note that discovery will be stopped during a connection setup. If the application tries
     * to re-initiate discovery during this time, it can fail.
     */
    public static final String WIFI_P2P_DISCOVERY_CHANGED_ACTION =
        "pt.utl.ist.cmov.wifidirect.DISCOVERY_STATE_CHANGE";

    /**
     * The lookup key for an int that indicates whether p2p discovery has started or stopped.
     * Retrieve it with {@link android.content.Intent#getIntExtra(String,int)}.
     *
     * @see #WIFI_P2P_DISCOVERY_STARTED
     * @see #WIFI_P2P_DISCOVERY_STOPPED
     */
    public static final String EXTRA_DISCOVERY_STATE = "discoveryState";
    
    /**
     * p2p discovery has stopped
     *
     * @see #WIFI_P2P_DISCOVERY_CHANGED_ACTION
     */
    public static final int WIFI_P2P_DISCOVERY_STOPPED = 1;

    /**
     * p2p discovery has started
     *
     * @see #WIFI_P2P_DISCOVERY_CHANGED_ACTION
     */
    public static final int WIFI_P2P_DISCOVERY_STARTED = 2;

    /**
     * Broadcast intent action indicating that this device details have changed.
     */
    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION =
        "pt.utl.ist.cmov.wifidirect.THIS_DEVICE_CHANGED";

    /**
     * The lookup key for a {@link android.net.wifi.p2p.WifiP2pDevice} object
     * Retrieve with {@link android.content.Intent#getParcelableExtra(String)}.
     */
    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";

    /**
     * The lookup key for a {@link android.net.wifi.p2p.WifiP2pDevice} object
     * Retrieve with {@link android.os.Bundle#getParcelable(String)}.
     * @hide
     */
    public static final String P2P_DEV_BUNDLE_KEY = "wifiP2pDevice";

    /**
     * The lookup key for a {@link android.net.wifi.p2p.WifiP2pConfig} object
     * Retrieve with {@link android.os.Bundle#getParcelable(String)}.
     * @hide
     */
    public static final String P2P_CONFIG_BUNDLE_KEY = "wifiP2pConfig";
}
