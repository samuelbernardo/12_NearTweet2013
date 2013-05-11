package pt.utl.ist.cmov.wifidirect;

import android.os.Parcelable;
import android.os.Parcel;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * A class representing a Wi-Fi P2p device list.
 *
 * Note that the operations are not thread safe.
 * {@see SimWifiP2pManager}
 */
public class SimWifiP2pDeviceList implements Parcelable {

    private HashMap<String, SimWifiP2pDevice> mDevices;

    public SimWifiP2pDeviceList() {
        mDevices = new HashMap<String, SimWifiP2pDevice>();
    }

    /** copy constructor */
    public SimWifiP2pDeviceList(SimWifiP2pDeviceList source) {
        if (source != null) {
            for (SimWifiP2pDevice d : source.getDeviceList()) {
                mDevices.put(d.deviceAddress, d);
            }
        }
    }

    /** @hide */
    public SimWifiP2pDeviceList(ArrayList<SimWifiP2pDevice> devices) {
        mDevices = new HashMap<String, SimWifiP2pDevice>();
        for (SimWifiP2pDevice device : devices) {
            if (device.deviceAddress != null) {
                mDevices.put(device.deviceAddress, device);
            }
        }
    }

    /** @hide */
    public void addList(ArrayList<SimWifiP2pDevice> devices) {
        for (SimWifiP2pDevice device : devices) {
            if (device.deviceAddress != null) {
                mDevices.put(device.deviceAddress, device);
            }
        }
    }

    /** @hide */
    public boolean clear() {
        if (mDevices.isEmpty()) return false;
        mDevices.clear();
        return true;
    }

    /** @hide */
    public void update(SimWifiP2pDevice device) {
        if (device == null || device.deviceAddress == null) return;
        SimWifiP2pDevice d = mDevices.get(device.deviceAddress);
        if (d != null) {
            d.deviceName = device.deviceName;
            return;
        }
        //Not found, add a new one
        mDevices.put(device.deviceAddress, device);
    }

    /** @hide */
    public void updateStatus(String deviceAddress, int status) {
        if (TextUtils.isEmpty(deviceAddress)) return;
        SimWifiP2pDevice d = mDevices.get(deviceAddress);
        if (d != null) {
            d.status = status;
        }
    }

    /** @hide */
    public SimWifiP2pDevice get(String deviceAddress) {
        if (deviceAddress == null) return null;

        return mDevices.get(deviceAddress);
    }

    /** @hide */
    public boolean remove(SimWifiP2pDevice device) {
        if (device == null || device.deviceAddress == null) return false;
        return mDevices.remove(device.deviceAddress) != null;
    }

    /** Get the list of devices */
    public Collection<SimWifiP2pDevice> getDeviceList() {
        return Collections.unmodifiableCollection(mDevices.values());
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        for (SimWifiP2pDevice device : mDevices.values()) {
            sbuf.append("\n").append(device);
        }
        return sbuf.toString();
    }

    /** Implement the Parcelable interface */
    public int describeContents() {
        return 0;
    }

    /** Implement the Parcelable interface */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDevices.size());
        for(SimWifiP2pDevice device : mDevices.values()) {
            dest.writeParcelable(device, flags);
        }
    }

    /** Implement the Parcelable interface */
    public static final Creator<SimWifiP2pDeviceList> CREATOR =
        new Creator<SimWifiP2pDeviceList>() {
            public SimWifiP2pDeviceList createFromParcel(Parcel in) {
                SimWifiP2pDeviceList deviceList = new SimWifiP2pDeviceList();

                int deviceCount = in.readInt();
                for (int i = 0; i < deviceCount; i++) {
                    deviceList.update((SimWifiP2pDevice)in.readParcelable(null));
                }
                return deviceList;
            }

            public SimWifiP2pDeviceList[] newArray(int size) {
                return new SimWifiP2pDeviceList[size];
            }
        };
}