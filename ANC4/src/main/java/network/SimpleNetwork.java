package network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A simplified point-to-point network.
 * 
 * Note that maps are used internally to enforce set property.
 *
 * @author Adam
 *
 */
public class SimpleNetwork implements Network {

    private Map<String, Node> nodes;	//Set of nodes in the network, by name.
    private Map<String, Link> links;	//Set of links in the network, by name.
    private boolean splitHorizon;		//Split-horizon capability flag.
    private List<Update> updateQueue;	//Queue of Routing Table broadcasts for next iteration.
    
    public SimpleNetwork(Map<String, Node> nodes, Map<String, Link> links) {
    	this.nodes = nodes;
    	this.links = links;
    	this.splitHorizon = false;
    	this.updateQueue = new LinkedList<Update>();
    	
    	//Populate Queue for initial discovery exchange.
    	populateQueue();
	}
    
	@Override
	public boolean exchange(int iterations) {
		//Check valid iterations.
		if (iterations <= 0) {
			String message = String.format("Invalid number of iterations: %s", iterations);
			System.out.println(message);
			System.out.println("Iterations must be > 0");
		}
		
		//Perform exchange <iterations> times.
		for (int i=0; i<iterations; i++) {
			//Execute Updates.
			while(!updateQueue.isEmpty()) {
				Update currentUpdate = updateQueue.remove(0);
				
				//Perform the update.
				//Iff split horizon capability active, then trim table before broadcasting.
				if (splitHorizon == true) {
					RoutingTable SHTable = new RoutingTable();
					
					//Iterate through entries, adding iff not outgoing on current link.
					for (Entry<String, TableEntry> entry : 
						currentUpdate.getSource().getTable().getTable().entrySet()) {
						
						if (entry.getValue().getOutgoingLink() != currentUpdate.getLink()) {
							SHTable.addEntry(entry.getKey(), entry.getValue());
						}
					}
					
					currentUpdate.getDest().dvUpdate(
							currentUpdate.getLink(), 
							SHTable);
				}
				//Else broadcast full table as normal.
				else {
					currentUpdate.getDest().dvUpdate(
						currentUpdate.getLink(), 
						currentUpdate.getSource().getTable());
				}
			}
			
			//Populate the queue with requested updates.
			//NOTE: we do first iteration without population for discovery exchange.
			populateQueue();
			
			//If stability achieved, then exit immediately.
			if (this.isStable()) {
				return true;
			}
		}
		
		//Exit, indicating stability.
		if (this.isStable()) {
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean exchangeWithTrace(int iterations, List<Node> nodesToTrace) {
		//Check valid iterations.
		if (iterations <= 0) {
			String message = String.format("Invalid number of iterations: %s", iterations);
			System.out.println(message);
			System.out.println("Iterations must be > 0");
		}
		
		//Perform exchange <iterations> times.
		for (int i=0; i<iterations; i++) {
			//Execute Updates.
			while(!updateQueue.isEmpty()) {
				Update currentUpdate = updateQueue.remove(0);
				
				//Perform the update.
				//Iff split horizon capability active, then trim table before broadcasting.
				if (splitHorizon == true) {
					RoutingTable SHTable = new RoutingTable();
					
					//Iterate through entries, adding iff not outgoing on current link.
					for (Entry<String, TableEntry> entry : 
						currentUpdate.getSource().getTable().getTable().entrySet()) {
						
						if (entry.getValue().getOutgoingLink() != currentUpdate.getLink()) {
							SHTable.addEntry(entry.getKey(), entry.getValue());
						}
					}
					
					currentUpdate.getDest().dvUpdate(
							currentUpdate.getLink(), 
							SHTable);
				}
				//Else broadcast full table as normal.
				else {
					currentUpdate.getDest().dvUpdate(
						currentUpdate.getLink(), 
						currentUpdate.getSource().getTable());
				}
			}
			
			//Populate the queue with requested updates.
			//NOTE: we do first iteration without population for discovery exchange.
			populateQueue();
			
			//##### Output to console #####
			StringBuilder builder = new StringBuilder();
			builder.append(String.format("##########\nIteration: %d\n\n", i));
				
			//Output the routing tables for requested nodes.
			nodesToTrace.forEach(node -> builder.append(node.toString() + "\n"));
			System.out.println(builder.toString());
			
			//If stability achieved, then exit immediately.
			if (this.isStable()) {
				return true;
			}
		}
		
		//Exit, indicating stability.
		if (this.isStable()) {
			return true;
		}
		else return false;
	}
	
	/**
	 * Populate the update queue with all pending requested updates.
	 * 
	 * i.e. iterate through all links, checking if nodes on either end
	 * need to broadcast an update across it. If so, then add to an update queue
	 * to be executed later, and set the flag in the node to indicate that it
	 * no longer requires an update.
	 * 
	 */
	private void populateQueue() {
		//Populate queue.
		for (Entry<String, Node> entry : nodes.entrySet()) {
			Node currentNode = entry.getValue();
			
			if (currentNode.getUpdated()) {
				//Iterate over links for each node.
				for (Link link : currentNode.getLinks()) {
					//Check if the link is down.
					//If down then do not do propagate updates across it.
					if (link.down == false) {
						//Copy the routing table entries.
						RoutingTable tableCopy = new RoutingTable();
						for (Entry<String, TableEntry> tableEntry : currentNode.getTable().getTable().entrySet()) {
							tableCopy.addEntry(tableEntry.getKey(),
									new TableEntry(tableEntry.getValue().getDestination(),
											tableEntry.getValue().getDistance(),
											tableEntry.getValue().getOutgoingLink()));
						}
						
						//Get the link node that is not this node, update TO it.
						if (link.getFirst() != currentNode) {
							updateQueue.add(new Update(currentNode, link.getFirst(), link));
						}
						else {
							updateQueue.add(new Update(currentNode, link.getSecond(), link));
						}
					}
				}
				
				//Updates queued so Node no longer requires.
				currentNode.setUpdated(false);
			}
		}
	}
	
	/**
	 * Construct a copy of a routing table. For use when creating updates to
	 * snapshot state of node at instance of time, to preserve current state.
	 * 
	 * @param node - the node whose table to copy.
	 * @return - a RoutingTable copy.
	 */
	private RoutingTable copyTable(Node node) {
		//TODO: split horizon
		//Copy the routing table entries.
		RoutingTable tableCopy = new RoutingTable();
		for (Entry<String, TableEntry> tableEntry : node.getTable().getTable().entrySet()) {
			tableCopy.addEntry(tableEntry.getKey(),
					new TableEntry(tableEntry.getValue().getDestination(),
							tableEntry.getValue().getDistance(),
							tableEntry.getValue().getOutgoingLink()));
		}
		
		return tableCopy;
	}
	
	@Override
	public void changeLinkCost(String linkName, int newCost) throws RuntimeException {
		Link link = links.get(linkName);

		if (link != null) {
			//Check for link down specification.
			if (newCost == Integer.MAX_VALUE) {
				link.setDown(true);
			}
			else {
				link.setDown(false);
			}
			
			link.setCost(newCost);
		}
		else {
			String message = String.format("Link not found: %s", linkName);
			throw new RuntimeException(message);
		}
	}
	
	@Override
	public List<Node> bestRoute(String sourceName, String destName) {
		List<Node> route = new ArrayList<Node>();
		Node currentNode = nodes.get(sourceName);
		
		//Check that the nodes exist.
		if (currentNode == null) {
			String message = String.format("Node not found: %s", sourceName);
			throw new RuntimeException(message);
		}
		if (nodes.get(destName) == null) {
			String message = String.format("Node not found: %s", destName);
			throw new RuntimeException(message);
		}
		
		//First node in route is source.
		route.add(currentNode);
		
		/*
		 * Go to next hop determined by current nodes routing table
		 * until the current node == destination.
		 */
		//TODO: Detect cycles.
		while(currentNode.getName().compareTo(destName) != 0) {
			//Get the next hop node.
			TableEntry entry = currentNode.getTable().getEntry(destName);
			
			//Check that the entry exists.
			if (entry == null) {
				/*
				 * If the entry doesn't exist and we haven't reach the dest yet,
				 * then we don't know a route to the node. Represent as a null
				 * entry at end of list.
				 */
				route.add(null);
				return route;
			}
			
			//Update the current node -> next hop.
			Link link = entry.getOutgoingLink();
			if (link.getFirst() == currentNode) currentNode = link.getSecond();
			else currentNode = link.getFirst();
			
			//Insert the new node into the route.
			route.add(currentNode);
		}		
		
		return route;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		String header = "FORMAT: (Node: [<Destination>,<Distance>,<Outgoing Link>], ..., [<...>])\n----------\n";
		builder.append(header);
		
		//For each node in network
		for (Entry<String, Node> entry : nodes.entrySet()) {
			builder.append(entry.getValue().toString());
			
			builder.append("\n");
		}
		
		return builder.toString();
	}

	@Override
	public boolean isStable() {
		return (updateQueue.size() == 0);
	}
	
	@Override
	public int numNodes() {
		return nodes.size();
	}
	
	@Override 
	public int numLinks() {
		return links.size();
	}

	@Override
	public Map<String, Node> getNodes() {
		return nodes;
	}
	@Override
	public Map<String, Link> getLinks() {
		return links;
	}
	@Override
	public void setSplitHorizon(boolean value) {
		this.splitHorizon = value;
	}
	@Override
	public boolean isSplitHorizon() {
		return splitHorizon;
	}
}
