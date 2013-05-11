package pt.utl.ist.cmov.wifidirect.console.commands;

import java.util.ArrayList;
import java.util.Collections;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Device;
import pt.utl.ist.cmov.wifidirect.console.Devices;

public class MoveCommand extends Command {

	public MoveCommand(String name, String abrv) {
		super(name,abrv);
	}

	public MoveCommand() {
		super("move","m");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 3) {
			printError("Wrong number of input arguments.");
			printHelp();
			return false;
		}
		
		// validate the target node
		String target = args[1];
		if (!devices.existsDevice(target)) {
			printError("Targeted device is not registered.");
			return false;
		}

		// parse neighbors list
		ArrayList<String> neighbors = new ArrayList<String>();
		String nlist = args[2];
		if (!nlist.startsWith("(") || !nlist.endsWith(")")) {
			printError("Neighbors list malformed.");
			printHelp();
			return false;
		}
		nlist = nlist.substring(1, nlist.length() - 1);
		if (nlist != null && !nlist.equals("")) {
			Collections.addAll(neighbors,nlist.split(","));
		}

		// check that the neighbors exist
		for (String neighbor : neighbors) {
			if (!devices.existsDevice(neighbor)) {
				printError("Device \"" + neighbor + "\" of the neighborhood list does not exist.");
				return false;
			}
		}

		// update the neighborhood list
		for (Device device : devices.getDevices()) {
			ArrayList<String> dn = device.getNeighbors();
			if (device.getName().equals(target)) {
				dn.clear();
				dn.addAll(neighbors);
			} else {
				dn.remove(target);
				if (neighbors.contains(device.getName())) {
					dn.add(target);
				}
			}
		}
		return true;
	}

	public void printHelp() {
		System.out.println("Syntax: move|m <target> (<peer_1>,...,<peer_n>)");
	}
}
