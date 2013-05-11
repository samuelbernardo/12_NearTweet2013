package pt.utl.ist.cmov.wifidirect.console.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

/**
 * This is the start command, which implements the main console loop
 *
 */
public class StartCommand extends Command {
	
	public StartCommand(String name, String abrv) {
		super(name,abrv);
	}

	public StartCommand() {
		super("-","-");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		String [] tokens = null;
		
		printIntro();
		while (true) {
			System.out.print(">");
			try {
				s = in.readLine();
				tokens = s.split(" ");
				if (tokens.length == 0) {
					continue;
				}
				String cmd = tokens[0];
				if (cmd.equals("")) {
					continue;
				}
				if (cmd.startsWith("#")) {
					continue;
				}
				boolean found = false;
				for (Command command : commands) {
					if (command.getName().equals(cmd) || command.getAbvr().equals(cmd)) {
						found = true;
						command.executeCommand(devices, commands, tokens);
						break;
					}
				}
				if (!found)
					Command.printWrongCommand(cmd);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error occurred!\n");
			}
		}
	}
	
	public static void printIntro() {
		System.out.println("WiFi Direct Simulator\n");
		System.out.println("  Working Directory = " + System.getProperty("user.dir"));
		System.out.println("  Type \"help\" or \"h\" for the full command list\n");
	}
}
