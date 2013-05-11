package pt.utl.ist.cmov.wifidirect.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import pt.utl.ist.cmov.wifidirect.console.exceptions.NeighborMalformedException;

public class Devices {

	private TreeMap<String,Device> mDevices;
	private ArrayList<String[]> mGroups;

	public Devices () {
		mDevices = new TreeMap<String,Device>();
		mGroups = new ArrayList<String[]>();
	}

	public void clear() {
		mDevices.clear();
		mGroups.clear();
	}

	public boolean checkDevice(String name) {
		return !mDevices.containsKey(name);
	}

	public boolean checkAddress(String ip, int port) {
		for (Device device : mDevices.values()) {
			if (device.getIp().equals(ip) && device.getPort() == port) {
				return false;
			}
		}
		return true;
	}

	public boolean existsDevice(String name) {
		return mDevices.containsKey(name);
	}

	public void addDevice(String name, String ip, int port) {
		assert !mDevices.containsKey(name) : "Check if device already exists";
		mDevices.put(name, new Device(name, ip, port));
	}
	
	public void removeDevice(String name) {
		mDevices.remove(name);
	}

	public Device getDevice(String name) {
		return mDevices.get(name);
	}

	public Collection<Device> getDevices() {
		return mDevices.values();
	}

	public int numDevices() {
		return mDevices.size();
	}
	
	public void setGroups(ArrayList<String[]> groups) {
		mGroups = groups;
	}
	
	public ArrayList<String[]> getGroups() {
		return mGroups;
	}
	
	public String marshalDeviceNeighbors(Device device) {
		StringBuilder sb = new StringBuilder(128);
		for (String neighbor : device.getNeighbors()) {
			Device ndev = getDevice(neighbor);
			sb.append(neighbor + ":" + ndev.getIp() + ":" + ndev.getPort() + "@");
		}
		return sb.toString();
	}

	public static ArrayList<Neighbor> unmarshalNeighbors(String msg)
			throws NeighborMalformedException {
		if (msg == null) {
			return new ArrayList<Neighbor>();
		}
		ArrayList<Neighbor> nb = new ArrayList<Neighbor>();
		try {
			String [] nbmsg = msg.split("@");
			for (String nbstr : nbmsg) {
				String [] fields = nbstr.split(":");
				Neighbor n = new Neighbor(fields[0], fields[1], 
						Integer.parseInt(fields[2]));
				nb.add(n);
			}
		} catch (Exception e) {
			throw new NeighborMalformedException();
		}
		return nb;
	}
}
