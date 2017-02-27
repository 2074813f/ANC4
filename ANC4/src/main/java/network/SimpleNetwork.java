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
		// TODO Auto-generated method stub
		
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

}
