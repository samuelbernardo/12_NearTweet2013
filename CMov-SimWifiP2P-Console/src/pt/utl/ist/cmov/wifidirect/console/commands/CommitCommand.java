package pt.utl.ist.cmov.wifidirect.console.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Device;
import pt.utl.ist.cmov.wifidirect.console.Devices;


/**
 * Propagate the neighborhood lists to all nodes
 *
 */
public class CommitCommand extends Command {

	private static int PROBE_TIMEOUT = 1000;
	
	public CommitCommand(String name, String abrv) {
		super(name,abrv);
	}

	public CommitCommand() {
		super("commit","c");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 1) {
			printError("Wrong number of input arguments.");
			return false;
		}
		
		// nothing to do if true
		if (devices.numDevices() == 0) {
			return true;
		}

		// check if the devices are online
		Hashtable<Thread,CommitWorker> workers = new Hashtable<Thread,CommitWorker>();
		for(Device dev : devices.getDevices()) {
			CommitWorker cw = new CommitWorker(dev,devices);
			Thread t = new Thread(cw);
	        t.start();
			workers.put(t, cw);
		}

		// wait for the termination of all threads
		for (Thread t : workers.keySet()) {
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}

		// display the results
		for(CommitWorker worker : workers.values()) {
			Device dev = worker.getDevice();
			boolean success = worker.getStatus();
			System.out.println(dev.getName() + "\t" + dev.getIp() + 
					"\t" + dev.getPort() + "\t" + 
					(success?"SUCCESS":"FAIL"));
		}
		return true;
	}

	class CommitWorker implements Runnable {

		private Device mDevice;
		private boolean mSuccess;
		private Devices mDevices;
		private String mMarshalled;

		public CommitWorker(Device device, Devices devices) {
			mDevice = device;
			mSuccess = true;
			mDevices = devices;
			mMarshalled = null;
		}

		public Device getDevice() {
			return mDevice;
		}
		
		public boolean getStatus() {
			return mSuccess;
		}

		public String getMarshalled() {
			return mMarshalled;
		}

		@Override
		public void run() {
			Socket client;
			PrintWriter printwriter;
			mMarshalled = mDevices.marshalDeviceNeighbors(mDevice);
			try {
				printMsg("Commiting to " + mDevice.getName() + "...");
				client = new Socket();
				client.connect(new InetSocketAddress(mDevice.getIp(), mDevice.getPort()),
						PROBE_TIMEOUT);
				printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.write(mMarshalled);
                printwriter.write("\n");
                printwriter.flush();
                printwriter.close();
				client.close();
				mSuccess = true;
			} catch (UnknownHostException e) {
				mSuccess = false;
			} catch (IOException e) {
				mSuccess = false;
			}
		}		
	}
}
