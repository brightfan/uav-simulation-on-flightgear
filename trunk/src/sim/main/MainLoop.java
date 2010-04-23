package sim.main;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.airport.KSFO;
import sim.flight.AutoPilot;
import sim.flight.Navigation;
import sim.flight.TakingOff;
import sim.globalvalue.GlobalValue;

public class MainLoop {

	public static boolean isDebugging;
	private static int debugMode = 0;

	public static void main(String[] args) {

		/* new plane */
		Aeroplane aeroplane = new Aeroplane(GlobalValue.inPort);
		aeroplane.setTakeoffSpeed((float) 73.5);
		Airport airport = new KSFO();

		isDebugging = true;
		debugMode = 2;

		if (isDebugging) {
			switch (debugMode) {
			case 1:
				AutoPilot autoPilot = new TakingOff(aeroplane, airport);
				autoPilot.initiateConnection(GlobalValue.destRawIP,
						GlobalValue.outPort);
				autoPilot.autoPilot();
				break;

			case 2:
				autoPilot = new Navigation(aeroplane, airport);
				autoPilot.initiateConnection(GlobalValue.destRawIP,
						GlobalValue.outPort);
				autoPilot.autoPilot();
				break;

			case 3:
				System.out.println("Debug mode 3 is not activated yet");
				break;

			default:
				System.out.println("Debug mode " + debugMode
						+ " is not recognizable");
			}
		}
		else {
			// TO DO: Real Navigating Code comes here.
		}
	}
}
