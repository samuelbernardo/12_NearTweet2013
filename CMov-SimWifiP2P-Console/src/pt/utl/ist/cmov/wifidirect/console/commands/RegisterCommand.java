package pt.utl.ist.cmov.wifidirect.console.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.utl.ist.cmov.wifidirect.console.Command;
import pt.utl.ist.cmov.wifidirect.console.Devices;

public class RegisterCommand extends Command {

	public RegisterCommand(String name, String abrv) {
		super(name,abrv);
	}

	public RegisterCommand() {
		super("register","r");
	}

	public boolean executeCommand(Devices devices, Command [] commands, String [] args) {
		assert devices == null || args == null: "Null arguments";

		// parse input arguments
		if (args.length != 4) {
			printError("Wrong number of input arguments.");
			return false;
		}
		
		// check the device name
		String deviceName = args[1];
		if (!devices.checkDevice(deviceName)) {
			printError("Device already registered with name \"" + deviceName + "\"");
			return false;
		}

		// check the ip address
		String ip = args[2];
		IPAddressValidator ipChecker = new IPAddressValidator();
		if (!ipChecker.validate(ip)) {
			printError("Wrong IP address format.");
			return false;
		}

		// check the port number
		int port = 0;
		try {
			port = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			printError("Wrong port number format.");
			return false;
		}

		// check the address and port number
		if (!devices.checkAddress(ip,port)) {
			printError("Device already registered with address \"" + ip + 
					":" + port + "\"");
			return false;
		}

		// register the device
		devices.addDevice(deviceName, ip, port);
		return true;
	}

	public void printHelp() {
		System.out.println("Syntax: register <device name> <IP> <port>");
	}
	
	class IPAddressValidator {
		 
	    private Pattern pattern;
	    private Matcher matcher;
	 
	    private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	 
	    public IPAddressValidator() {
		  pattern = Pattern.compile(IPADDRESS_PATTERN);
	    }
	 
	   /**
	    * Validate ip address with regular expression
	    * @param ip ip address for validation
	    * @return true valid ip address, false invalid ip address
	    */
	    public boolean validate(final String ip) {		  
		  matcher = pattern.matcher(ip);
		  return matcher.matches();	    	    
	    }
	}
}
