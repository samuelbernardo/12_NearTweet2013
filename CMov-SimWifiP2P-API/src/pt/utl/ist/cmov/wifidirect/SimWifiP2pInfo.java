package pt.utl.ist.cmov.wifidirect;

import android.os.Parcelable;
import android.os.Parcel;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A class representing connection information about a Wi-Fi p2p group
 *
 * {@see WifiP2pManager}
 */
public class SimWifiP2pInfo implements Parcelable {

    /** Indicates if a p2p group has been successfully formed */
    public boolean groupFormed;

    /** Indicates if the current device is the group owner */
    public boolean isGroupOwner;

    /** Group owner address */
    public InetAddress groupOwnerAddress;

    public SimWifiP2pInfo() {
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("groupFormed: ").append(groupFormed)
            .append("isGroupOwner: ").append(isGroupOwner)
            .append("groupOwnerAddress: ").append(groupOwnerAddress);
        return sbuf.toString();
    }

    /** Implement the Parcelable interface */
    public int describeContents() {
        return 0;
    }

    /** copy constructor */
    public SimWifiP2pInfo(SimWifiP2pInfo source) {
        if (source != null) {
            groupFormed = source.groupFormed;
            isGroupOwner = source.isGroupOwner;
            groupOwnerAddress = source.groupOwnerAddress;
       }
    }

    /** Implement the Parcelable interface */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(groupFormed ? (byte)1 : (byte)0);
        dest.writeByte(isGroupOwner ? (byte)1 : (byte)0);

        if (groupOwnerAddress != null) {
            dest.writeByte((byte)1);
            dest.writeByteArray(groupOwnerAddress.getAddress());
        } else {
            dest.writeByte((byte)0);
        }
    }

    /** Implement the Parcelable interface */
    public static final Creator<SimWifiP2pInfo> CREATOR =
        new Creator<SimWifiP2pInfo>() {
            public SimWifiP2pInfo createFromParcel(Parcel in) {
                SimWifiP2pInfo info = new SimWifiP2pInfo();
                info.groupFormed = (in.readByte() == 1);
                info.isGroupOwner = (in.readByte() == 1);
                if (in.readByte() == 1) {
                    try {
                        info.groupOwnerAddress = InetAddress.getByAddress(in.createByteArray());
                    } catch (UnknownHostException e) {}
                }
                return info;
            }

            public SimWifiP2pInfo[] newArray(int size) {
                return new SimWifiP2pInfo[size];
            }
        };
}
