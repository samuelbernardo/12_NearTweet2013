package pt.utl.ist.cmov.wifidirect.console.commands;

import java.util.concurrent.TimeUnit;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Blocks the WDSim console for the amount of time specified by the user.
 *
 */
public class WaitCommand extends Command {
	
	public WaitCommand(String name, String abrv) {
		super(name,abrv);
	}

	public WaitCommand() {
		super("wait","w");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		if (args.length != 2 && args.length != 3) {
			printError("Wrong number of input arguments.");
			return false;
		}

		// check the input time
		int time = 1;
		try {
			time = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			printError("Wrong time value.");
			return false;
		}
		
		// check the time units
		if (args.length == 3) {
			String unit = args[2];
			if (!unit.equals("s")) {
				if (unit.equals("m")) {
					time *= 60;
				} else {
					printError("Wrong time unit.");
					return false;
				}
			}
		}

		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			printError("Wait interrupted.");
			return false;
		}
		return true;
	}

	public void printHelp() {
		System.out.println("Syntax: wait|w <time> [s|m]");
	}
}
