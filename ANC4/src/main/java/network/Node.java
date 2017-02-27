package network;

import routing.table.RoutingTable;

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

	public String getName() {
		return name;
	}
	public RoutingTable getTable() {
		return table;
	}
}
