package sim.main;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.airport.KSFO;
import sim.flight.AutoPilot;
import sim.flight.TakingOff;
import sim.globalvalue.GlobalValue;

public class MainLoop {
	public static void main(String [] args) {
		
		/* new plane */
		Aeroplane aeroplane = new Aeroplane(GlobalValue.inPort);
		aeroplane.setTakeoffSpeed((float)73.5);
		Airport airport = new KSFO();
		
		/*while (true) {
			aeroplane.readStatus();
			
			System.out.println("current heading is: " + aeroplane.getHeadingDeg());
			System.out.println("current ground speed is: " + aeroplane.getGroundSpeedKt());
			//System.out.println("current desired heading is: " + airport.direction);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
		AutoPilot autoPilot = new TakingOff(aeroplane, airport);
		autoPilot.initiateConnection(GlobalValue.destRawIP, GlobalValue.outPort);
		
		autoPilot.autoPilot();
	}
}
