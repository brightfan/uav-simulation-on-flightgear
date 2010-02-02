package testCases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sim.aircraft.Aeroplane;
import sim.globalvalue.GlobalValue;

public class AeroplaneTest {
	private Aeroplane testPlane;
	
	@Before
	public void setUp() {
		testPlane = new Aeroplane(GlobalValue.inPort);
	}
	
	@Test
	public void readStatusTest() {
		assertTrue(testPlane.readStatus());
		System.out.println(testPlane.getAirSpeedKt());
	}
	
}
