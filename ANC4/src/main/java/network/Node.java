package network;

import java.util.Map.Entry;

import routing.table.RoutingTable;
import routing.table.TableEntry;

/**
 * A node in a simplified
 * point-to-point network.
 *
 * @author Adam
 *
 */
public class Node implements Device {
	
	private String name;
	private RoutingTable table;
	private boolean updated;
	
	public Node(String name) {
		this.name = name;
		this.table = new RoutingTable();
		this.updated = true;		//Set to true initially to cause broadcast to neighbors.
		
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
	 *TODO: update params
	 * @param entry - the incoming table to check.
	 */
	public void dvUpdate(Link link, RoutingTable incomingTable) {
		//Assume no routing table update occurred until it does.
		this.updated = false;
		
		for (Entry<String, TableEntry> entry : incomingTable.getTable().entrySet()) {
			//Distance to this node = <link cost> + <entry distance>.
			//see: Bellman-Ford algorithm. 
			int newDistance = link.getCost() + entry.getValue().getDistance();
			String nodeName = entry.getValue().getDestination().getName();
			
			/* Update the table if one of 3 conditions are met:
			 * 	1. If node hasn't been seen , add entry
			 * 	2. If new cost < existing cost, update entry.
			 * 	3. If new route learned over same link as current best route, update.
			 */
			TableEntry existing = this.table.getEntry(nodeName);
			if (existing == null 
					|| newDistance < existing.getDistance() 
					|| link == existing.getOutgoingLink()) {
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
}
