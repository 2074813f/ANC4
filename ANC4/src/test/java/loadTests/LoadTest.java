package loadTests;

import static org.junit.Assert.*;

import org.junit.Test;

import network.Network;
import util.DataReader;

/**
 * Set of tests that check the capability to read in a file
 * describing a simple point-to-point network.
 * 
 * @author Adam
 *
 */
public class LoadTest {
	
	@Test
	public void loadNetwork() {
		String testFile = "src/main/resource/testset1";
		
		Network newNetwork = DataReader.parseFile(testFile);
		
		assertTrue(newNetwork.numNodes() == 2);
		assertTrue(newNetwork.numLinks() == 1);
	}

}
