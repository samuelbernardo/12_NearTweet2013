package pt.utl.ist.cmov.wifidirect.console.commands;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Quits the console application
 *
 */
public class QuitCommand extends Command {
	
	public QuitCommand(String name, String abrv) {
		super(name,abrv);
	}

	public QuitCommand() {
		super("quit","q");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";
		System.exit(0);
		return true;
	}
}
