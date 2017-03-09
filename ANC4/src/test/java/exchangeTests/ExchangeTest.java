package exchangeTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import network.Link;
import network.Network;
import network.Node;
import network.SimpleNetwork;
import routing.table.RoutingTable;
import routing.table.TableEntry;
import util.DataReader;

/**
 * Set of tests that check the "exchange" functionality of a network.
 * 
 * i.e. the ability to simulate routing table exchanges between nodes
 * on the network.
 * 
 * @author Adam
 *
 */
public class ExchangeTest {
	
	Network network;		//Network as read from file @T=0
	
	@Before
	public void setUp() {
		String testFile = "src/main/resource/testset1";
		
		network = DataReader.parseFile(testFile);
	}
	
	@Test
	public void testSingleExchange() {	
		network.exchange(1);
		
		Node n1 = network.getNodes().get("N1");
		Node n2 = network.getNodes().get("N2");
		Link l1 = network.getLinks().get("L1");
		
		TableEntry n1n2 = n1.getTable().getTable().get(n2.getName());
		TableEntry n2n1 = n2.getTable().getTable().get(n1.getName());
		
		//Check that n2 is correctly added to n1's RT.
		assertTrue(n1n2.getDestination().equals(n2));
		assertTrue(n1n2.getDistance() == 5);
		assertTrue(n1n2.getOutgoingLink().equals(l1));

		//Check that n1 is correctly added to n2's RT.
		assertTrue(n2n1.getDestination().equals(n1));
		assertTrue(n2n1.getDistance() == 5);
		assertTrue(n2n1.getOutgoingLink().equals(l1));
	}
	
	/**
	 * Test that updating the cost of a link is propogated correctly.
	 */
	@Test
	public void testUpdateCost() {
		network.exchange(1);
		
		Node n1 = network.getNodes().get("N1");
		Node n2 = network.getNodes().get("N2");
		Link l1 = network.getLinks().get("L1");
		
		//Check the entry for N2 in N1's RT before change
		TableEntry expectedN2 = new TableEntry(n2, 5, l1);
		assertTrue(n1.getTable().getEntry("N2").equals(expectedN2));
		
		//Check the entry for N1 in N2's RT before change
		TableEntry expectedN1 = new TableEntry(n1, 5, l1);
		assertTrue(n2.getTable().getEntry("N1").equals(expectedN1));
		
		//Do the link cost update.
		l1.setCost(4);
		
		//Check the entry for N2 in N1's RT after change
		expectedN2 = new TableEntry(n2, 4, l1);
		assertTrue(n1.getTable().getEntry("N2").equals(expectedN2));
		
		//Check the entry for N1 in N2's RT after change
		expectedN1 = new TableEntry(n1, 4, l1);
		assertTrue(n2.getTable().getEntry("N1").equals(expectedN1));
	}
}
