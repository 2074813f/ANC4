package routing.table;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import network.Link;
import network.Node;

/**
 * The routing table for a node in a simplified point-to-point
 * network. Holds a collection of entries corresponding to routes
 * to destination nodes.
 * 
 * @author Adam
 *
 */
public class RoutingTable {
	
	private Map<Node, TableEntry> table;
	
	public RoutingTable() {
		this.table = new HashMap<Node, TableEntry>();
	}
	
	/**
	 * Update the routing table for this node by iterating through an
	 * incoming table and checking each entry.
	 * 
	 * @param entry - the incoming table to check.
	 */
	public void updateTable(Link link, Node node) {
		RoutingTable incomingTable = node.getTable();
		
		//Iterate through each entry in the table.
		for (Entry<Node, TableEntry> entry : incomingTable.getTable().entrySet()) {
			
			//Distance to this node = <link cost> + <entry distance>.
			int newDistance = link.getCost() + entry.getValue().getDistance();
			
			/* Construct a new entry with:
			 * 	- dest = table entry node
			 * 	- distance = <link cost> + <entry distance>
			 * 	- outgoingLink = link
			 */
			TableEntry newEntry = new TableEntry(entry.getKey(), newDistance, link);
			
			//If node doesn't exist OR if new cost < existing cost, add/update entry.
			TableEntry existing = this.table.get(entry.getKey());
			if ((existing == null) || (newDistance < existing.getDistance())) {
				this.table.put(entry.getKey(), newEntry);
			}
		}
	}
	
	public void addEntry(Node node, TableEntry entry) {
		this.table.put(node, entry);
	}
	
	public Map<Node, TableEntry> getTable() {
		return this.table;
	}
}
