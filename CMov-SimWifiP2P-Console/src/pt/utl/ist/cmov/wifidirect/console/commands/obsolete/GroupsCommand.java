package pt.utl.ist.cmov.wifidirect.console.commands.obsolete;

import java.util.ArrayList;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Device;
import pt.utl.ist.cmov.wifidirect.console.Devices;

public class GroupsCommand extends Command {

	public GroupsCommand(String name, String abrv) {
		super(name,abrv);
	}

	public GroupsCommand() {
		super("groups","g");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		// parse input arguments
		ArrayList<String[]> groups = new ArrayList<String[]> ();
		for (int i = 1; i < args.length; i++) {
			String group_str = args[i];
			String [] clients_str = group_str.split(":");
			groups.add(clients_str);
		}
		
		// check for names not registered
		String devNotRegistered = null;
		for (String [] g : groups) {
			for (String client : g) {
				if (!devices.existsDevice(client)) {
					devNotRegistered = client;
					break;
				}
			}
		}
		if (devNotRegistered != null) {
			printError("Device \"" + devNotRegistered + "\" is not registered.");
			return false;
		}

		// check for completion and duplicates
		ArrayList<String> visited = new ArrayList<String> ();
		for (Device device : devices.getDevices()) {
			boolean found = false;
			for (String [] g : groups) {
				for (String client : g) {
					if (client.equals(device.getName())) {
						if (visited.contains(client)) {
							printError("Device \"" + client + "\" is in several groups.");
							return false;
						} else {
							visited.add(device.getName());
							found = true;
							break;
						}
					}
				}
			}
			if (found) {
				continue;
			} else {
				printError("Device \"" + device.getName() + "\" must be in a group.");
				return false;
			}
		}

		devices.setGroups(groups);
		System.out.println("Ready (" + groups.size() + " groups)");
		return true;
	}

	public void printHelp() {
		System.out.println("Syntax: groups|g <group sequences>");
	}
}
