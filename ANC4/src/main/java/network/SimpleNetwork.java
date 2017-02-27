package network;

import java.util.List;
import java.util.Map;

/**
 * A simplified point-to-point network.
 * 
 * Note that maps are used internally to enforce set property.
 *
 * @author Adam
 *
 */
public class SimpleNetwork implements Network {

    private Map<String, Node> nodes;	//Set of nodes in the network, by name.
    private Map<String, Link> links;	//Set of links in the network, by name.
    private boolean splitHorizon;		//Split-horizon capability flag.
    
    public SimpleNetwork(Map<String, Node> nodes, Map<String, Link> links) {
    	this.nodes = nodes;
    	this.links = links;
    	this.splitHorizon = false;
	}
    
	@Override
	public void exchange(int iterations) {
		//Perform exchange <iterations> times.
		for (int i=0; i<iterations; i++) {
			//Exchange information between nodes on link.
			links.entrySet().forEach(entry -> {
				Node first = entry.getValue().getFirst();
				Node second = entry.getValue().getSecond();
				
				first.updateTable(entry.getValue(), second);
				second.updateTable(entry.getValue(), first);
			});
		}
	}
	@Override
	public void changeLinkCost(Link link, int newCost) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<Device> bestRoute(Node source, Node destination) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int numNodes() {
		return nodes.size();
	}
	
	@Override 
	public int numLinks() {
		return links.size();
	}

	@Override
	public Map<String, Node> getNodes() {
		return nodes;
	}
	@Override
	public Map<String, Link> getLinks() {
		return links;
	}
	@Override
	public boolean isSplitHorizon() {
		return splitHorizon;
	}
}
