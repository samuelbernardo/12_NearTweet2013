package pt.utl.ist.cmov.wifidirect.console;

import java.util.ArrayList;

public class Device {
	
	private String mName;
	private String mIp;
	private int mPort;
	private ArrayList<String> mNeighbors;

	public Device(String name, String ip, int port) {
		mName = name;
		mIp = ip;
		mPort = port;
		mNeighbors = new ArrayList<String>();
	}
	
	public String getName() {
		return mName;
	}
	
	public String getIp() {
		return mIp;
	}
	
	public int getPort() {
		return mPort;
	}
	
	public ArrayList<String> getNeighbors() {
		return mNeighbors;
	}

	public void print() {
		System.out.println("Device = [Name:" + mName + ", IP:" + mIp +
			", Port:" + mPort);
	}
}
