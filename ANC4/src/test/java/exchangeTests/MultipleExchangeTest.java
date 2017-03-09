package exchangeTests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import network.Link;
import network.Network;
import network.Node;
import routing.table.TableEntry;
import util.DataReader;

public class MultipleExchangeTest {
	
	Network network;		//Network as read from file @T=0
	
	@Before
	public void setUp() {
		String testFile = "src/main/resource/testset2";
		
		network = DataReader.parseFile(testFile);
	}
	
	/**
	 * Test that updating the cost of a link is propagated correctly,
	 * after 2 exchanges.
	 */
	@Test
	public void testUpdateCostT2() {
		//Arbitrary exchanges until stable.
		network.exchange(5);
		
		Node n1 = network.getNodes().get("N1");
		Node n2 = network.getNodes().get("N2");
		Node n3 = network.getNodes().get("N3");
		Link l1 = network.getLinks().get("L1");
		Link l2 = network.getLinks().get("L2");
		Link l3 = network.getLinks().get("L3");
		
		//Check the entry for N2 in N1's RT before change
		TableEntry expectedN2 = new TableEntry(n2, 1, l1);
		assertTrue(n1.getTable().getEntry("N2").equals(expectedN2));
		
		//Check the entry for N3 in N2's RT before change
		TableEntry expectedN3 = new TableEntry(n3, 3, l1);
		assertTrue(n1.getTable().getEntry("N3").equals(expectedN3));
		
		//Do the link cost update.
		l1.setCost(8);
		
		//Check the entry for N2 in N1's RT after change
		expectedN2 = new TableEntry(n2, 8, l1);
		assertTrue(n1.getTable().getEntry("N2").equals(expectedN2));
		
		//Check the entry for N1 in N2's RT after change
		TableEntry expectedN1 = new TableEntry(n1, 8, l1);
		assertTrue(n2.getTable().getEntry("N1").equals(expectedN1));
		
		//Check the entry for N1 in N3's RT after change
		//NOTE: Expect no change since no new exchange.
		expectedN1 = new TableEntry(n1, 3, l2);
		assertTrue(n3.getTable().getEntry("N1").equals(expectedN1));
		
		//Do another exchange
		//N3 should learn that N1 is cheapest on L3 now.
		network.exchange(2);
		
		//Check the entry for N1 in N3's RT after change
		expectedN1 = new TableEntry(n1, 5, l3);
		assertTrue(n3.getTable().getEntry("N1").equals(expectedN1));
	}

}
