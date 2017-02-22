package network;

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
     **/
    public void exchange(int iterations);

    /**
     * Update the cost of a link.
     **/
    public void changeLinkCost(Link link, int newCost);

    /**
     * Provide the best route from one Node to another across
     * the network.
     *
     * @param source - the source node
     * @param destination - the destination node
     **/
     public List<Device> bestRoute(Node source, Node destination);
}
