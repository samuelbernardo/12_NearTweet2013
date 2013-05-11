package pt.utl.ist.cmov.wifidirect.console.commands;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * Print the list of supported commands
 *
 */
public class HelpCommand extends Command {
	
	public HelpCommand(String name, String abrv) {
		super(name,abrv);
	}

	public HelpCommand() {
		super("help","h");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		for (Command cmd : commands) {
			System.out.println(cmd.getName() + " (" + cmd.getAbvr() + ")");
		}
		return true;
	}
}
