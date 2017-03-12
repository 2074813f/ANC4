package network;

import java.util.Map.Entry;

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
	
	@Override
	public boolean equals(Object o) {
		//self check
		if (this == o) return true;
		//null check
		if (o == null) return false;
		//type check and cast
		if (getClass() != o.getClass()) return false;
		TableEntry entry = (TableEntry) o;
		
		//Check fields
		if (this.getDestination() != entry.getDestination()) return false;
		if (this.getDistance() !=  entry.getDistance()) return false;
		if (this.getOutgoingLink() != entry.getOutgoingLink()) return false;
		
		//All fields were equal so entries are equal.
		return true;
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
	
	@Override
	public String toString() {
		String stringRepr = String.format("{%s, %s, %s}", 
				this.destination.getName(), this.distance, this.outgoingLink);
		
		return stringRepr;
	}
}
