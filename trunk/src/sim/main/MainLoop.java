package sim.main;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.airport.KSFO;
import sim.flight.AutoPilot;
import sim.flight.Landing;
import sim.flight.LandingEnhanced;
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
		/* new airport */
		Airport airport = new KSFO();

		isDebugging = false;
		debugMode = 4;

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
				autoPilot = new Landing(aeroplane, airport);
				autoPilot.initiateConnection(GlobalValue.destRawIP,
						GlobalValue.outPort);
				autoPilot.autoPilot();
				break;
				
			case 4:
				autoPilot = new LandingEnhanced(aeroplane, airport);
				autoPilot.initiateConnection(GlobalValue.destRawIP,
						GlobalValue.outPort);
				autoPilot.autoPilot();
				break;

			default:
				System.out.println("Debug mode " + debugMode
						+ " is not recognizable");
			}
		}
		else {
			AutoPilot takeOff = new TakingOff(aeroplane, airport);
			takeOff.initiateConnection(GlobalValue.destRawIP,
					GlobalValue.outPort);
			AutoPilot navigation = takeOff.autoPilot();
			navigation.initiateConnection(GlobalValue.destRawIP,
					GlobalValue.outPort);
			AutoPilot landing = navigation.autoPilot();
			landing.initiateConnection(GlobalValue.destRawIP,
					GlobalValue.outPort);
			landing.autoPilot();
		}
	}
}
