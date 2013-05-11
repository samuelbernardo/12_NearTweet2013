package pt.utl.ist.cmov.wifidirect;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * A class representing Wi-Fi Protected Setup
 *
 * {@see SimWifiP2pConfig}
 */
public class SimWpsInfo implements Parcelable {

    /** Push button configuration */
    public static final int PBC     = 0;
    /** Display pin method configuration - pin is generated and displayed on device */
    public static final int DISPLAY = 1;
    /** Keypad pin method configuration - pin is entered on device */
    public static final int KEYPAD  = 2;
    /** Label pin method configuration - pin is labelled on device */
    public static final int LABEL   = 3;
    /** Invalid configuration */
    public static final int INVALID = 4;

    /** Wi-Fi Protected Setup. www.wi-fi.org/wifi-protected-setup has details */
    public int setup;

    /** @hide */
    public String BSSID;

    /** Passed with pin method configuration */
    public String pin;

    public SimWpsInfo() {
        setup = INVALID;
        BSSID = null;
        pin = null;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(" setup: ").append(setup);
        sbuf.append('\n');
        sbuf.append(" BSSID: ").append(BSSID);
        sbuf.append('\n');
        sbuf.append(" pin: ").append(pin);
        sbuf.append('\n');
        return sbuf.toString();
    }

    /** Implement the Parcelable interface */
    public int describeContents() {
        return 0;
    }

    /* Copy constructor */
    public SimWpsInfo(SimWpsInfo source) {
        if (source != null) {
            setup = source.setup;
            BSSID = source.BSSID;
            pin = source.pin;
        }
    }

    /** Implement the Parcelable interface */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(setup);
        dest.writeString(BSSID);
        dest.writeString(pin);
    }

    /** Implement the Parcelable interface */
    public static final Creator<SimWpsInfo> CREATOR =
        new Creator<SimWpsInfo>() {
            public SimWpsInfo createFromParcel(Parcel in) {
                SimWpsInfo config = new SimWpsInfo();
                config.setup = in.readInt();
                config.BSSID = in.readString();
                config.pin = in.readString();
                return config;
            }

            public SimWpsInfo[] newArray(int size) {
                return new SimWpsInfo[size];
            }
        };
}