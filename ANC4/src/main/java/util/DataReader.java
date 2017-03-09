package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import network.Link;
import network.Network;
import network.Node;
import network.SimpleNetwork;

/**
 * Class exposing functionality to read files describing networks.
 * 
 * @author Adam
 *
 */
public class DataReader {
	
	/**
	 * Read in a file and construct a new Network from it.
	 * 
	 * @param filename
	 * @return - a new Network or null if an error occurred during parsing.
	 */
	public static Network parseFile(String filename) {
		int lineNumber = 0;
		
		Map<String, Node> nodes = new HashMap<String, Node>();
		Map<String, Link> links = new HashMap<String, Link>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String currentLine = reader.readLine();
			String[] tokens;
			
			while(currentLine != null) {
				tokens = currentLine.split(" ");
				
				if (tokens[0].compareTo("node") == 0) {
					String name = tokens[1];
					
					nodes.put(name, new Node(name));
				}
				else if (tokens[0].compareTo("link") == 0) {
					String name = tokens[1];
					String node1_name = tokens[2];
					String node2_name = tokens[3];
					int cost = Integer.parseInt(tokens[4]);
					
					Node first = nodes.get(node1_name);
					Node second = nodes.get(node2_name);
					
					//If not existing nodes, add to nodes.
					if (first == null) {
						first = new Node(node1_name);
						nodes.put(node1_name, first);
					}
					if (second == null) {
						second = new Node(node2_name);
						nodes.put(node2_name, second);
					}
					
					links.put(name, new Link(name, first, second, cost));
				}
				else {
					String message = String.format("Invalid type token: %s", tokens[0]);
					throw new IllegalArgumentException(message);
				}
				
				lineNumber++;
				currentLine = reader.readLine();
			}
			
			//File read, now construct network.
			Network newNetwork = new SimpleNetwork(nodes, links);
			return newNetwork;
			
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found: " + filename);
		} catch (IOException e) {
			System.err.println("Error reading file: " + filename);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			String message = String.format("Illegal argument in input file at line %s: - %s", lineNumber, e.getMessage());
			System.err.println(message);
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Illegal number of arguments given at line: %s", lineNumber);
			System.err.println(message);
		}
		
		return null;
	}

}
