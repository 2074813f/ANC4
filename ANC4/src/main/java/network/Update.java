package network;

/**
 * Object that simulates an update packet - an exchange of a
 * RoutingTable from a Node on a given link to a another Node.
 * 
 * @author Adam
 *
 */
public class Update {
	
	private Node source;
	private Node dest;
	private Link link;
	private RoutingTable table;
	
	public Update(Node source, Node dest, Link link) {
		this.source = source;
		this.dest = dest;
		this.link = link;
		this.table = table;
	}
	
	
	public Node getSource() {
		return source;
	}
	public Node getDest() {
		return dest;
	}
	public Link getLink() {
		return link;
	}
	public RoutingTable getTable() {
		return table;
	}
}
