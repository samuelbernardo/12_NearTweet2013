package pt.utl.ist.cmov.wifidirect.console.commands;

import java.util.ArrayList;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Device;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Prints the devices that are currently registered or the peers of each device
 *
 */
public class PrintCommand extends Command {
	
	public PrintCommand(String name, String abrv) {
		super(name,abrv);
	}

	public PrintCommand() {
		super("print","p");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 2) {
			printError("Wrong number of input arguments.");
			printHelp();
			return false;
		}
		
		String option = args[1];
		if (option.equals("devices") || option.equals("d")) {
			printDevices(devices,commands,args);
			return true;
		}
		if (option.equals("neighbors") || option.equals("n")) {
			printNeighbors(devices,commands,args);
			return true;
		}
		printError("Unknown option \"" + option + "\"");
		return false;
	}

	protected void printDevices(Devices devices, Command [] commands, String [] args) {
		for(Device dev : devices.getDevices()) {
			System.out.println(dev.getName() + "\t" + dev.getIp() + 
					"\t" + dev.getPort());
		}
	}

	protected void printGroups(Devices devices, Command [] commands, String [] args) {
		ArrayList<String[]> groups = devices.getGroups();
		for (String [] group : groups) {
			int i = 0;
			for (String client : group) {
				System.out.print(client);
				i++;
				if (i < group.length) {
					System.out.print(":");
				}
			}
			System.out.print(" ");
		}
		System.out.println();
	}

	protected void printNeighbors(Devices devices, Command [] commands, String [] args) {
		for (Device device: devices.getDevices()) {
			System.out.print(device.getName() + " => ");
			ArrayList<String> neighbors = device.getNeighbors();
			int i = 0;
			for (String client : neighbors) {
				System.out.print(client);
				i++;
				if (i < neighbors.size()) {
					System.out.print(",");
				}
			}
			System.out.println();
		}
	}
	
	public void printHelp() {
		System.out.println("Syntax: print|p <what>");
		System.out.println("   devices|d, neighbors|n");
	}
}

