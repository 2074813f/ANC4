package routing.table;

import network.Link;
import network.Node;

/**
 * An entry in a routing table of a Node,
 * describing routing information to a destination
 * node from the current node.
 * 
 * @author Adam
 *
 */
public class TableEntry {
	
	private Node destination;
	private int distance;
	private Link outgoingLink;
	
	public TableEntry(Node dest, int distance, Link outgoingLink){
		this.destination = dest;
		this.distance = distance;
		this.outgoingLink = outgoingLink;
	}
	
	public Node getDestination() {
		return destination;
	}
	public void setDestination(Node destination) {
		this.destination = destination;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public Link getOutgoingLink() {
		return outgoingLink;
	}
	public void setOutgoingLink(Link outgoingLink) {
		this.outgoingLink = outgoingLink;
	}
}
