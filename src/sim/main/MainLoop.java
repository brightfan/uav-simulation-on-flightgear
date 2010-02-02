package sim.main;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.airport.KSFO;
import sim.globalvalue.GlobalValue;

public class MainLoop {
	public static void main(String [] args) {
		
		/* new plane */
		Aeroplane aeroplane = new Aeroplane(GlobalValue.inPort);
		Airport airport = new KSFO();
		
	}
}
