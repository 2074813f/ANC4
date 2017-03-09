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
		
		for (int i=0; i<4; i++) {
			System.out.println("Iteration: " + i);
			System.out.println("Stable: " + network.isStable());
			System.out.println(network.toString());
			network.exchange(1);
		}

		//TODO: start simulation, step through
		//TODO: CLI interface

	}

}
