package network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import routing.table.TableEntry;

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
				currentUpdate.getDest().dvUpdate(
						currentUpdate.getLink(), 
						currentUpdate.getSource().getTable());
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
	
	private void populateQueue() {
		//Populate queue.
		for (Entry<String, Link> entry : links.entrySet()) {
			Link link = entry.getValue();
			
			//If the routing table has changed, node requires broadcast.
			if (link.getFirst().getUpdated() == true) {
				updateQueue.add(new Update(link.getFirst(), link.getSecond(), link));
			}
			if (link.getSecond().getUpdated() == true) {
				updateQueue.add(new Update(link.getSecond(), link.getFirst(), link));
			}
		}
		
		//Set updated to false for all nodes, since we have queued updates for them.
		for (Entry<String, Node> entry : nodes.entrySet()) {
			entry.getValue().setUpdated(false);
		}
	}
	
	@Override
	public void changeLinkCost(String linkName, int newCost) throws RuntimeException {
		Link link = links.get(linkName);

		if (link != null) {
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
		return updateQueue.size() == 0;
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
	public boolean isSplitHorizon() {
		return splitHorizon;
	}
}
