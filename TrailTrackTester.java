// --== CS400 File Header Information ==--
// Name: Lucy Jiang
// Email: jiang68@wisc.edu

import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** 
 * This class to test if TrailTrack is behaving like we expected
 * using JUnit test
 */
public class TrailTrackTester {
	protected TrailTrack _instance = null;

	// set up HashTable of trails using test trails data
	@BeforeEach
	public void createInstance() {
		_instance = new TrailTrack(10, "trail_test.csv");
	}	

	// test 1: test if add node will change size
	// test if add repeated node will not change size
	@Test
	public void testAdd() {
		int old = _instance.size();
		Node<String> testNode = new Node<String>("Pheasant Branch Conservancy",3.1,2,7.121,28.64,1,0,1,1);
		_instance.put(testNode);
		assertEquals(old+1, _instance.size());
		// add repeated node
		_instance.put(testNode);
		assertEquals(old+1, _instance.size());
	}

	// test 2: test if remove invalid name will not change size
	// test if remove valid name will change size
	@Test
	public void testRemove() {
		int old;
	        old = _instance.size();
		_instance.remove("Pinic Point");
		assertEquals(old, _instance.size());
		_instance.remove("Picnic Point");
		assertEquals(old-1, _instance.size());
	}

	// test 3: test if filter give the right number of trails
	@Test
	public void testFilter() {
		int count = _instance.filterList(3,1,3);
		assertEquals(3, count);

	}

	// test 4: test if updateReview() can change review
	// getReview() gives expected results
	@Test
	public void testRate() {
		assertEquals(0, _instance.getReview("Owen Conservation Park"));
		_instance.updateReview("Owen Conservation Park", 3);
		assertEquals(3, _instance.getReview("Owen Conservation Park"));
	}

	// test 5: test if list give the right number of trails
	@Test
	public void testList() {
		assertEquals(10, _instance.listAll());
	}

}
