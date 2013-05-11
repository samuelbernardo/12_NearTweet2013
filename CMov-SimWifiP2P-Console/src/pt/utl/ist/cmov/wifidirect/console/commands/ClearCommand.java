package pt.utl.ist.cmov.wifidirect.console.commands;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Blocks the WDSim console for the amount of time specified by the user.
 *
 */
public class ClearCommand extends Command {
	
	public ClearCommand(String name, String abrv) {
		super(name,abrv);
	}

	public ClearCommand() {
		super("clear","e");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 1) {
			printError("Wrong number of input arguments.");
			return false;
		}

		devices.clear();
		return true;
	}
}
