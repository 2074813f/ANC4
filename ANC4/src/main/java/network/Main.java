package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import util.DataReader;

public class Main {
	
	final static String options = "Options:\n"
			+ "- \"exchange\" <no. iterations>\n"
			+ "- \"splithorizon\" <true|false>\n" 	//TODO
			+ "- \"best-route\" <source> <dest>\n"
			+ "- \"exchange-with-change\" <no. of iterations> <link to change> <new cost> <fail after>\n"
			+ "- \"exchange-with-trace\" <no. of iterations> <node 1> <node 2> ... <node n>\n"
			+ "- \"network\"\n"
			+ "- \"exit\"";

	public static void main(String[] args) {
		String filename = args[0];
		Network network = DataReader.parseFile(filename);
		
		//Check that parsing was successful.
		if (network == null) {
			System.err.println("No network established.");
			System.exit(-1);
		}
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
			System.out.println("Please enter a command or type \"options\":");
			String input = reader.readLine();
			
			//While exit command not issued, continue to ask for input.
			while(true) {
				String[] tokens = input.split(" ");
				
				//##### "help" #####
				if (tokens[0].compareTo("options") == 0) {
					System.out.println(options);
				}
				//##### "quit" #####
				else if (tokens[0].compareTo("exit") == 0 || tokens[0].compareTo("quit") == 0) {
					System.exit(0);
				}
				//##### "exchange" #####
				else if (tokens[0].compareTo("exchange") == 0) {
					boolean stable = network.exchange(Integer.parseInt(tokens[1]));
					
					String output = String.format("Completed exchange(s), exited with network stable: %s", stable);
					System.out.println(output);
				}
				//##### "exchange-with-change" #####
				else if (tokens[0].compareTo("exchange-with-changes") == 0) {
					//Do <fail after> iterations first
					network.exchange(Integer.parseInt(tokens[4]));
					
					//Change link cost.
					network.changeLinkCost(tokens[2], Integer.parseInt(tokens[3]));
					
					//Do the remaining iterations
					boolean stable = network.exchange(Integer.parseInt(tokens[1]) - Integer.parseInt(tokens[4]));
					
					String output = String.format("Completed exchange(s), exited with network stable: %s", stable);
					System.out.println(output);
				}
				//##### "exchange-with-trace" #####
				else if (tokens[0].compareTo("exchange-with-trace") == 0) {
					List<Node> nodesToTrace = new ArrayList<Node>();
					
					//Get the list of nodes to trace.
					for (int i=2; i<tokens.length; i++) {
						nodesToTrace.add(network.getNodes().get(tokens[i]));
					}
					
					boolean stable = network.isStable();
					
					//Do the iterations
					for (int iter=0; iter<Integer.parseInt(tokens[1]); iter++) {
						StringBuilder builder = new StringBuilder();
						builder.append(String.format("##########\nIteration: %d\n\n", iter));
						
						//Do one iteration.
						stable = network.exchange(1);
						
						//Output the routing tables for requested nodes.
						nodesToTrace.forEach(node -> builder.append(node.toString() + "\n"));
						
						System.out.println(builder.toString());
					}
					
					String output = String.format("Completed exchange(s), exited with network stable: %s", stable);
					System.out.println(output);
				}
				//##### "best-route" #####
				else if (tokens[0].compareTo("best-route") == 0) {
					List<Node> route = network.bestRoute(tokens[1], tokens[2]);
					
					StringBuilder builder = new StringBuilder();
					builder.append(String.format("Best route from %s -> %s: ",tokens[1], tokens[2]));
					route.forEach(node -> {
						if (node == null) {
							builder.append("- , ");
							builder.append(tokens[2] + " , ");
						}
						else builder.append(node.getName() + " , ");
					});
					builder.deleteCharAt(builder.length() - 1);
					builder.deleteCharAt(builder.length() - 1);
					
					System.out.println(builder.toString());
				}
				else if (tokens[0].compareTo("network") == 0) {
					System.out.println(network.toString());
				}
				//NO MATCH
				else {
					System.out.println("Invalid command.");
				}
				
				input = reader.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
