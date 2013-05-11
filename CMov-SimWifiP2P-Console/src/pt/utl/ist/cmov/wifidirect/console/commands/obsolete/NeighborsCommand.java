package pt.utl.ist.cmov.wifidirect.console.commands.obsolete;

import java.util.ArrayList;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Device;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Prints the current groups
 * @author nsantos
 *
 */
public class NeighborsCommand extends Command {
	
	public NeighborsCommand(String name, String abrv) {
		super(name,abrv);
	}

	public NeighborsCommand() {
		super("neighbors","n");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 1) {
			printError("Wrong number of input arguments.");
			return false;
		}

		for (Device device: devices.getDevices()) {
			System.out.print(device.getName() + "\t=>");
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

		return true;
	}
}
