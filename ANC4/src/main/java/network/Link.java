package network;

/**
 * Interface describing a link in a simplified
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
