package network;

/**
 * A link in a simplified
 * point-to-point network.
 *
 * @author Adam
 *
 */
public class Link {

	String name;
	Node first;
	Node second;
	int cost;
	
	boolean down;	//Is the link down.
	
	public Link(String name, Node first, Node second, int cost) {
		this.name = name;
		this.first = first;
		this.second = second;
		this.cost = cost;
		
		this.down = false;
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
	
	/**
	 * Updates the cost associated with the link.
	 * 
	 * NOTE: for simplicity this causes the RT entries for
	 * linked neighbors to be updated/checked immediately.
	 * 
	 * @param cost
	 */
	public void setCost(int cost) {
		//Change in cost = old cost - new cost.
		//Difference is then summed with old entries to give new distance.
		int costChange = cost - this.cost;
		
		first.linkUpdate(this, costChange);
		second.linkUpdate(this, costChange);
		
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

	public boolean isDown() {
		return down;
	}
	public void setDown(boolean down) {
		this.down = down;
	}
}
