package pt.utl.ist.cmov.wifidirect;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * A class representing a Wi-Fi P2p configuration for setting up a connection
 *
 * {@see WifiP2pManager}
 */
public class SimWifiP2pConfig implements Parcelable {

    /**
     * The device MAC address uniquely identifies a Wi-Fi p2p device
     */
    public String deviceAddress;

    /**
     * Wi-Fi Protected Setup information
     */
    public SimWpsInfo wps;

    /**
     * This is an integer value between 0 and 15 where 0 indicates the least
     * inclination to be a group owner and 15 indicates the highest inclination
     * to be a group owner.
     *
     * A value of -1 indicates the system can choose an appropriate value.
     */
    public int groupOwnerIntent = -1;

    /**
     * Indicates whether the configuration is saved
     * @hide
     */
    public enum Persist {
        SYSTEM_DEFAULT,
        YES,
        NO
    }

    /** @hide */
    public Persist persist = Persist.SYSTEM_DEFAULT;

    public SimWifiP2pConfig() {
        //set defaults
        wps = new SimWpsInfo();
        wps.setup = SimWpsInfo.PBC;
    }

    /** P2P-GO-NEG-REQUEST 42:fc:89:a8:96:09 dev_passwd_id=4 {@hide}*/
    public SimWifiP2pConfig(String supplicantEvent) throws IllegalArgumentException {
        String[] tokens = supplicantEvent.split(" ");

        if (tokens.length < 2 || !tokens[0].equals("P2P-GO-NEG-REQUEST")) {
            throw new IllegalArgumentException("Malformed supplicant event");
        }

        deviceAddress = tokens[1];
        wps = new SimWpsInfo();

        if (tokens.length > 2) {
            String[] nameVal = tokens[2].split("=");
            int devPasswdId;
            try {
                devPasswdId = Integer.parseInt(nameVal[1]);
            } catch (NumberFormatException e) {
                devPasswdId = 0;
            }
            //Based on definitions in wps/wps_defs.h
            switch (devPasswdId) {
                //DEV_PW_USER_SPECIFIED = 0x0001,
                case 0x01:
                    wps.setup = SimWpsInfo.DISPLAY;
                    break;
                //DEV_PW_PUSHBUTTON = 0x0004,
                case 0x04:
                    wps.setup = SimWpsInfo.PBC;
                    break;
                //DEV_PW_REGISTRAR_SPECIFIED = 0x0005
                case 0x05:
                    wps.setup = SimWpsInfo.KEYPAD;
                    break;
                default:
                    wps.setup = SimWpsInfo.PBC;
                    break;
            }
        }
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n address: ").append(deviceAddress);
        sbuf.append("\n wps: ").append(wps);
        sbuf.append("\n groupOwnerIntent: ").append(groupOwnerIntent);
        sbuf.append("\n persist: ").append(persist.toString());
        return sbuf.toString();
    }

    /** Implement the Parcelable interface */
    public int describeContents() {
        return 0;
    }

    /** copy constructor */
    public SimWifiP2pConfig(SimWifiP2pConfig source) {
        if (source != null) {
            deviceAddress = source.deviceAddress;
            wps = new SimWpsInfo(source.wps);
            groupOwnerIntent = source.groupOwnerIntent;
            persist = source.persist;
        }
    }

    /** Implement the Parcelable interface */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceAddress);
        dest.writeParcelable(wps, flags);
        dest.writeInt(groupOwnerIntent);
        dest.writeString(persist.name());
    }

    /** Implement the Parcelable interface */
    public static final Creator<SimWifiP2pConfig> CREATOR =
        new Creator<SimWifiP2pConfig>() {
            public SimWifiP2pConfig createFromParcel(Parcel in) {
                SimWifiP2pConfig config = new SimWifiP2pConfig();
                config.deviceAddress = in.readString();
                config.wps = (SimWpsInfo) in.readParcelable(null);
                config.groupOwnerIntent = in.readInt();
                config.persist = Persist.valueOf(in.readString());
                return config;
            }

            public SimWifiP2pConfig[] newArray(int size) {
                return new SimWifiP2pConfig[size];
            }
        };
}