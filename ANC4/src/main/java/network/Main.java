package network;

import util.DataReader;

public class Main {

	public static void main(String[] args) {
		String filename = args[0];
		Network network = DataReader.parseFile(filename);
		
		//Check that parsing was successful.
		if (network == null) {
			System.err.println("No network established.");
			System.exit(-1);
		}

		//TODO: start simulation, step through
		//TODO: CLI interface

	}

}
