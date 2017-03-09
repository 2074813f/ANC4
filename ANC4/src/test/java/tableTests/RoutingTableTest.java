package tableTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import network.Network;
import network.Node;
import routing.table.RoutingTable;
import util.DataReader;

public class RoutingTableTest {
	
	Network network;
	
	@Before
	public void setUp() {
		String testFile = "src/main/resource/testset1";
		
		network = DataReader.parseFile(testFile);
	}
	
	/**
	 * Test the getEntry() method of a routing table.
	 * 
	 * Assumes at start-up a node should have itself in its routing table.
	 */
	@Test
	public void testGetEntry() {
		//Get the routingTable for an arbitrary node.
		Node n1 = network.getNodes().get("N1");
		RoutingTable table = n1.getTable();
		
		assertTrue(table.getEntry("N1") != null);
	}
	
	/**
	 * Test that getEntry() returns null if a node is not listed
	 * in the routing table.
	 * 
	 */
	@Test
	public void testNoEntry() {
		//Get the routingTable for an arbitrary node.
		Node n1 = network.getNodes().get("N1");
		RoutingTable table = n1.getTable();
		
		assertTrue(table.getEntry("L1") == null);
	}
}
