package routing.table;

import java.util.HashMap;
import java.util.Map;

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
	 * Update the routing table for this node to include a new
	 * entry or update an existing entry.
	 * 
	 * @param entry - the update entry value.
	 */
	public void updateTable(TableEntry entry) {
		//TODO: update table to reflect new entry.
	}

}
