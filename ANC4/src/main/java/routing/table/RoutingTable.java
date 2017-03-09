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
	
	private Map<String, TableEntry> table;
	
	public RoutingTable() {
		this.table = new HashMap<String, TableEntry>();
	}
	
	/**
	 * Add a new entry to the routing table.
	 * 
	 * @param nodeName - the name of the new node.
	 * @param entry - the entry to input to the table.
	 */
	public void addEntry(String nodeName, TableEntry entry) {
		this.table.put(nodeName, entry);
	}
	
	/**
	 * Try to get a node from the routing table.
	 * 
	 * @param nodeName
	 * @return - the TableEntry associated with the node, or null
	 * 			 if not found.
	 */
	public TableEntry getEntry(String nodeName) {
		return table.get(nodeName);
	}
	
	public Map<String, TableEntry> getTable() {
		return this.table;
	}
	
	@Override
	public boolean equals(Object o) {
		//self check
		if (this == o) return true;
		//null check
		if (o == null) return false;
		//type check and cast
		if (getClass() != o.getClass()) return false;
		RoutingTable table = (RoutingTable) o;
		
		//Check size of tables.
		if (this.getTable().size() != table.getTable().size()) return false;
		
		//Check each entry.
		for(Entry<String, TableEntry> entry : table.getTable().entrySet()) {
			TableEntry current = this.getEntry(entry.getKey());
			
			//If missing entry or entry values not equal then tables not equal.
			if (current == null || current != entry.getValue()) {
				return false;
			}
		}
		
		//All entries were equal so tables are equal.
		return true;
	}
}
