package stabilityTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import network.Link;
import network.Network;
import network.Node;
import routing.table.TableEntry;
import util.DataReader;

public class ConvergenceTest {
	
	Network network;		//Network as read from file @T=0
	
	@Before
	public void setUp() {
		String testFile = "src/main/resource/testset3";
		
		network = DataReader.parseFile(testFile);
	}
	
	/**
	 * Test that for an example where routes are advertised to nodes with
	 * failed links.
	 * 
	 * Should see slow convergence.
	 */
	@Test
	public void testUpdateCostT2() {
		//Arbitrary exchanges until stable.
		network.exchange(5);
		
		Node n1 = network.getNodes().get("N1");
		Node n2 = network.getNodes().get("N2");
		Node n3 = network.getNodes().get("N3");
		Node n4 = network.getNodes().get("N4");
		Link l1 = network.getLinks().get("L1");
		Link l2 = network.getLinks().get("L2");
		Link l3 = network.getLinks().get("L3");
		
		//Expect:     1    1    1
		//         N1---N2---N3---N4
		
		//Check the entry for N4 in N3's RT before change
		//i.e. ---N3-(1)-N4
		TableEntry expectedN4 = new TableEntry(n4, 1, l3);
		assertTrue(n3.getTable().getEntry("N4").equals(expectedN4));
		
		//Check the entry for N3 in N2's RT before change
		//i.e. ---N2-(1)-N3---
		TableEntry expectedN3 = new TableEntry(n3, 1, l2);
		assertTrue(n2.getTable().getEntry("N3").equals(expectedN3));
		
		//Do the link cost update.
		l1.setCost(2);
		l3.setCost(8);
		
		//Check the entry for N2 in N1's RT after change
		expectedN4 = new TableEntry(n4, 8, l3);
		assertTrue(n3.getTable().getEntry("N4").equals(expectedN4));
		
		//Check the entry for N3 in N2's RT before change
		//i.e. ---N2-(1)-N3---
		//NOTE: Expect no change since no new exchange.
		assertTrue(n2.getTable().getEntry("N3").equals(expectedN3));
		
		//Do another exchange
		//N2 should poison N3s link route to N4.
		network.exchange(2);
		
		//Check the entry for N1 in N3's RT after change
		expectedN4 = new TableEntry(n4, 3, l2);
		assertEquals(expectedN4, n3.getTable().getEntry("N4"));
	}

}
