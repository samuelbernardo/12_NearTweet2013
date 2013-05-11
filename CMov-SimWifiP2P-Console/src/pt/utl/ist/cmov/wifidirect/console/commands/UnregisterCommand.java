package pt.utl.ist.cmov.wifidirect.console.commands;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Unregisters a currently registered device from the console
 *
 */
public class UnregisterCommand extends Command {
	
	public UnregisterCommand(String name, String abrv) {
		super(name,abrv);
	}

	public UnregisterCommand() {
		super("unreg","u");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 2) {
			printError("Wrong number of input arguments.");
			return false;
		}

		String deviceName = args[1];
		if (devices.checkDevice(deviceName)) {
			printError("No device registered with name \"" + deviceName + "\"");
			return false;
		}
		devices.removeDevice(deviceName);
		return true;
	}
}
