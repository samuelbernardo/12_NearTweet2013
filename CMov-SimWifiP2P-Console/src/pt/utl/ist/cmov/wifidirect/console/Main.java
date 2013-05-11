package pt.utl.ist.cmov.wifidirect.console;

import pt.utl.ist.cmov.wifidirect.console.commands.*;

public class Main {

	public static void main(String[] args) {

		// supported commands
		Command [] commands = {
				new ClearCommand(),
				new CommitCommand(),
				new HelpCommand(),
				new LoadCommand(),
				new MoveCommand(),
				new PingCommand(),
				new PrintCommand(),
				new QuitCommand(),
				new RegisterCommand(),
				new UnregisterCommand(),
				new WaitCommand(),
		};

		// program state
		Devices devices = new Devices();
		
		// the root command
		StartCommand rootcmd = new StartCommand();
		rootcmd.executeCommand(devices, commands, args);
	}
}
