package network;

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
	
	String name;
	RoutingTable table;
	
	public Node(String name) {
		this.name = name;
		this.table = new RoutingTable();
		
		//Add an entry for self in routing table.
		this.table.addEntry(this, new TableEntry(this, 0, null));
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
	 * Convenience Method.
	 * 
	 * Compare the current routing table against an adjacent nodes
	 * table.
	 * 
	 * @param incomingTable - the incoming table to check.
	 */
	public void updateTable(Link link, Node node) {
		this.table.updateTable(link, node);
	}

	public String getName() {
		return name;
	}
	public RoutingTable getTable() {
		return table;
	}
}
