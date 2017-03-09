package network;

import java.util.List;
import java.util.Map;

/**
 * Interface describing a simplified point-to-point
 * network.
 *
 * @author Adam
 *
 */
public interface Network {

    /**
     * Make an exchange between devices on the network,
     * i.e. devices exchange routing information.
     *
     * @param iterations - no. of exchanges to do...
     * @return - true if stability was achieved in <= iterations
     * 			 else false.
     **/
    public boolean exchange(int iterations);
    
    /**
     * As: exchange(...), but traces each iteration for a given
     * set of nodes.
     * 
     * @param iterations - no. of exchanges to do...
     * @param nodesToTrace - the list of nodes whose routing tables to output to console.
     * @return - true if stability was achieved in <= iterations
     * 			 else false.
     */
    public boolean exchangeWithTrace(int iterations, List<Node> nodesToTrace);

    /**
     * Update the cost of a link.
     * 
     * @param linkName - the name of the link to update
     * @param newCost - the new cost to associate with that link
     **/
    public void changeLinkCost(String linkName, int newCost);

    /**
     * Provide the best route from one Node to another across
     * the network.
     *
     * @param sourceName - the name of the source node
     * @param destName - the name of the destination node
     **/
     public List<Node> bestRoute(String sourceName, String destName);
     
     /**
      * Return the number of nodes in the network.
      * @return - no. of nodes
      */
     public int numNodes();
     
     /**
      * Return the number of links in the network.
      * @return - no. of links
      */
     public int numLinks();
     
 	/**
 	 * Stability status of the network, i.e. are there still 
 	 * pending updates.
 	 * 
 	 * @return - true if network is stable else false.
 	 */
     public boolean isStable();
     
 	public Map<String, Node> getNodes();
 	
	public Map<String, Link> getLinks();
	
	public void setSplitHorizon(boolean value);
	public boolean isSplitHorizon();
}
