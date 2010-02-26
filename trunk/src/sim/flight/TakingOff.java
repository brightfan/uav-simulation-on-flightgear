package sim.flight;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;

public class TakingOff extends AutoPilot {

	private GroundHeadingControl groundHeadingControl;
	private RollDegsControl rollDegsControl;

	/* stage control */
	boolean stage1 = true; /* gears leave ground */
	boolean stage2 = false; /* climb to safety height */
	boolean stage3 = false; /* keep on the same height */

	public TakingOff(Aeroplane aeroplane, Airport airport) {
		super(aeroplane, airport);
		groundHeadingControl = new GroundHeadingControl(0.2, 0.005, 10.0);
		rollDegsControl = new RollDegsControl(0.1, 0.0002, 5.0);
	}

	@Override
	public void autoPilot() {
		while (true) {
			aeroplane.readStatus();

			if (stage1) {
				if (aeroplane.getGroundSpeedKt() > aeroplane.getTakeoffSpeed()) {
					stage1 = false;
					stage2 = true;
					System.out.println("Entered stage 2!");
				} else {
					throttle = (float) 0.8;
					rudder = (float) groundHeadingControl
							.getResult(airport.direction
									- aeroplane.getHeadingDeg());
					aileron = 0;
					elevator = 0;
					brakeParking = 0;
				}
			}
			
			if (stage2) {
				throttle = (float) 1.0;
				rudder = 0;
				aileron = (float) rollDegsControl.getResult(0 - aeroplane.getRollDeg());
				elevator = (float) 0.0;
				brakeParking = 1;
			}

			// System.out.println("throttle is: " + this.throttle);
			// System.out.println("current heading is: " +
			// aeroplane.getHeadingDeg());
			// System.out.println("current desired heading is: " +
			// airport.direction);
			// System.out.println("rudder is: " + this.rudder);
			// System.out.println("aileron is: " + this.aileron);
			// System.out.println("elevator is: " + this.elevator);
			// System.out.println("brakingParking is: " + this.brakeParking);
			/*System.out.println("current speed is: "
					+ aeroplane.getGroundSpeedKt());
			System.out.println("current height is: "
					+ aeroplane.getAltitudeAglFt()); */
			System.out.println("current roll degree speed is: "
					+ aeroplane.getRollRateDegps());
			System.out.println("current roll degree is: "
					+ aeroplane.getRollDeg());
			
			sendCommand();

		}
	}
}
