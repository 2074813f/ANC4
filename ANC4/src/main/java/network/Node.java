package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * A node in a simplified
 * point-to-point network.
 *
 * @author Adam
 *
 */
public class Node {
	
	private String name;
	private RoutingTable table;
	private boolean updated;
	
	private List<Link> links;
	
	public Node(String name) {
		this.name = name;
		this.table = new RoutingTable();
		this.updated = true;		//Set to true initially to cause broadcast to neighbors.
		this.links = new ArrayList<Link>();
		
		//Add an entry for self in routing table.
		this.table.addEntry(name, new TableEntry(this, 0, null));
	}
	
	/**
	 * Name comparison is used.
	 */
	@Override
	public boolean equals(Object o) {
		//self check
		if (this == o) return true;
		//null check
		if (o == null) return false;
		//type check and cast
		if (getClass() != o.getClass()) return false;
		Node node = (Node) o;
		return this.name.equals(node.getName());
	}
	
	/**
	 * Simulate a DV entry update from another node reaching this node.
	 * 
	 * The routing table should be updated if the cost of the
	 * link the update is received on + distance given is <
	 * current distance.
	 * 
	 * @param link - the link that the DV is from.
	 * @param incomingTable - the routing table from the neighbor.
	 */
	public void dvUpdate(Link link, RoutingTable incomingTable) {
		for (Entry<String, TableEntry> entry : incomingTable.getTable().entrySet()) {
			//Distance to this node = <link cost> + <entry distance>.
			//see: Bellman-Ford algorithm.
			int newDistance;
			if (link.isDown()) {
				newDistance = Integer.MAX_VALUE;
			}
			else {
				newDistance = link.getCost() + entry.getValue().getDistance();
			}
			String nodeName = entry.getValue().getDestination().getName();
			
			/* Update the table if one of 3 conditions are met:
			 * 	1. If node hasn't been seen , add entry
			 * 	2. If new cost < existing cost, update entry.
			 * 	3. If new route learned over same link as current best route 
			 * 	   and distance != existing distance, update.
			 */
			TableEntry existing = this.table.getEntry(nodeName);
			
			if (existing == null) {
				/* Construct a new entry with:
				 * 	- dest = table entry node
				 * 	- distance = <link cost> + <entry distance>
				 * 	- outgoingLink = link
				 */
				TableEntry newEntry = new TableEntry(entry.getValue().getDestination(), newDistance, link);
				
				this.table.addEntry(nodeName, newEntry);
				this.updated = true;
			}
			else if ((newDistance < existing.getDistance()) 
					|| ((link == existing.getOutgoingLink()) && (newDistance != existing.getDistance()))) {
				
				/* Construct a new entry with:
				 * 	- dest = table entry node
				 * 	- distance = <link cost> + <entry distance>
				 * 	- outgoingLink = link
				 */
				TableEntry newEntry = new TableEntry(entry.getValue().getDestination(), newDistance, link);
				
				this.table.addEntry(nodeName, newEntry);
				this.updated = true;
			}
		}
	}
	
	/**
	 * TODO
	 * 
	 * @param link
	 * @param dest
	 */
	public void linkUpdate(Link link, int costChange) {
		//Iterate over entries, if outgoing link is the given link, then update dist.
		for (Entry<String, TableEntry> entry : this.getTable().getTable().entrySet()) {
			TableEntry current = entry.getValue();
			
			//If the node is using the link to neighbor, update its cost.
			if (current.getOutgoingLink() == link) {
				current.setDistance(current.getDistance() + costChange);
				this.updated = true;
			}
		}
	}

	public String getName() {
		return this.name;
	}
	public RoutingTable getTable() {
		return this.table;
	}
	public boolean getUpdated() {
		return this.updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	public List<Link> getLinks() {
		return this.links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public void addLink(Link link) {
		this.links.add(link);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(name + ": ");
		
		//For each TableEntry in RT for node.
		for (Entry<String, TableEntry> nodeEntry : table.getTable().entrySet()) {
			builder.append('[');
			builder.append(nodeEntry.getValue().getDestination().getName() + ", ");	//Destination
			builder.append(nodeEntry.getValue().getDistance() + ", ");				//Distance
			
			Link outgoing = nodeEntry.getValue().getOutgoingLink();
			String output = (outgoing == null) ? "~" : outgoing.getName();
			builder.append(output);													//Outgoing Link
			
			builder.append(']');
		}
		
		return builder.toString();
	}
}
