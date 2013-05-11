package pt.utl.ist.cmov.wifidirect;

import android.os.Parcel;
import android.os.Parcelable;

public class SimWifiP2pDevice implements Parcelable {

    /**
     * The device name is a user friendly string to identify a Wi-Fi p2p device
     */
    public String deviceName = "";

    /**
     * The device address uniquely identifies a Wi-Fi p2p device
     */
    public String deviceAddress = "";
    
    public static final int CONNECTED   = 0;
    public static final int INVITED     = 1;
    public static final int FAILED      = 2;
    public static final int AVAILABLE   = 3;
    public static final int UNAVAILABLE = 4;

    /** Device connection status */
    public int status = UNAVAILABLE;


    public SimWifiP2pDevice() {
    }

    public SimWifiP2pDevice(String string) throws IllegalArgumentException {
        String[] tokens = string.split(":");

        if (tokens.length != 3) {
            throw new IllegalArgumentException("Malformed device specification");
        }
        deviceName = tokens[0];
        deviceAddress = tokens[1] + ":" + tokens[2];
        status = AVAILABLE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SimWifiP2pDevice)) return false;

        SimWifiP2pDevice other = (SimWifiP2pDevice) obj;
        if (other == null || other.deviceAddress == null) {
            return (deviceAddress == null);
        }
        return other.deviceAddress.equals(deviceAddress);
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Device: ").append(deviceName);
        sbuf.append("\n deviceAddress: ").append(deviceAddress);
        sbuf.append("\n status: ").append(status);
        return sbuf.toString();
    }

    /** Implement the Parcelable interface */
    public int describeContents() {
        return 0;
    }

    /** copy constructor */
    public SimWifiP2pDevice(SimWifiP2pDevice source) {
        if (source != null) {
            deviceName = source.deviceName;
            deviceAddress = source.deviceAddress;
            status = source.status;
        }
    }

    /** Implement the Parcelable interface */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceName);
        dest.writeString(deviceAddress);
        dest.writeInt(status);
    }

    /** Implement the Parcelable interface */
    public static final Creator<SimWifiP2pDevice> CREATOR =
        new Creator<SimWifiP2pDevice>() {
            public SimWifiP2pDevice createFromParcel(Parcel in) {
                SimWifiP2pDevice device = new SimWifiP2pDevice();
                device.deviceName = in.readString();
                device.deviceAddress = in.readString();
                device.status = in.readInt();
                return device;
            }

            public SimWifiP2pDevice[] newArray(int size) {
                return new SimWifiP2pDevice[size];
            }
        };
}
