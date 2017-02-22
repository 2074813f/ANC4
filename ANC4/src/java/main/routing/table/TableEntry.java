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
	private float distance;
	private Link outgoingLink;
	
	public TableEntry(){
	}
	
	public Node getDestination() {
		return destination;
	}
	public void setDestination(Node destination) {
		this.destination = destination;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public Link getOutgoingLink() {
		return outgoingLink;
	}
	public void setOutgoingLink(Link outgoingLink) {
		this.outgoingLink = outgoingLink;
	}
}
