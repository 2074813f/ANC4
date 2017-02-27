package exchangeTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import network.Link;
import network.Network;
import network.Node;
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
	
	Network network;
	
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
		
		TableEntry n1n2 = n1.getTable().getTable().get(n2);
		TableEntry n2n1 = n2.getTable().getTable().get(n1);
		
		//Check that n2 is correctly added to n1's RT.
		assertTrue(n1n2.getDestination().equals(n2));
		assertTrue(n1n2.getDistance() == 5);
		assertTrue(n1n2.getOutgoingLink().equals(l1));

		//Check that n1 is correctly added to n2's RT.
		assertTrue(n2n1.getDestination().equals(n1));
		assertTrue(n2n1.getDistance() == 5);
		assertTrue(n2n1.getOutgoingLink().equals(l1));
	}

}
