package pt.utl.ist.cmov.wifidirect.console;

public abstract class Command {

	protected String mName;
	protected String mAbvr;
	protected boolean mIsInternal;

	public Command(String name, String abvr) {
		assert name == null : "Invalid command argument";
		mName = name;
		mAbvr = abvr;
	}

	public String getName() {
		return mName;
	}

	public String getAbvr() {
		return mAbvr;
	}
	
	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || commands == null : "Invalid command argument";
		return true;
	}

	public void printHelp() {
		System.out.println("Syntax: "+ mName + "|" + mAbvr);
	}

	public static void printWrongCommand(String cmd) {
		System.out.println("Error: Command \"" + cmd + "\" does not exist.");
	}

	public void printError(String msg) {
		System.out.println("Error: " + msg);
	}

	public void printMsg(String msg) {
		//System.out.println(msg);
	}
}
