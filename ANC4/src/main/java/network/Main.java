package network;

import util.DataReader;

public class Main {

	public static void main(String[] args) {
		//TODO: Read in a file with nodes and links.
		String filename = args[0];
		Network network = DataReader.parseFile(filename);
		
		//Check that parsing was successful.
		if (network == null) System.exit(-1);
		
		//TODO: Add nodes to new network

		//TODO: start simulation, step through
		//TODO: CLI interface

	}

}
