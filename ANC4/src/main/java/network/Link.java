package network;

/**
 * A link in a simplified
 * point-to-point network.
 *
 * @author Adam
 *
 */
public class Link implements Device {

	String name;
	Node first;
	Node second;
	int cost;
	
	public Link(String name, Node first, Node second, int cost) {
		this.name = name;
		this.first = first;
		this.second = second;
		this.cost = cost;
	}
	
	/**
	 * Broadcast routing tables between the two adjacent nodes.
	 * 
	 * i.e. broadcasts routing table of first -> second and
	 * second -> first.
	 */
	public void broadcastUpdate() {
		
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
		Link link = (Link) o;
		return this.name.equals(link.getName());
	}

	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getName() {
		return name;
	}
	public Node getFirst() {
		return first;
	}
	public Node getSecond() {
		return second;
	}
}
