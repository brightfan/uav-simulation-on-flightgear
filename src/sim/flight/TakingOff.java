package sim.flight;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.controller.GroundHeadingControl;
import sim.controller.PitchDegsControl;
import sim.controller.RollDegsControl;
import sim.controller.YawDegsControl;
import sim.utils.KMLFileWritter;

public class TakingOff extends AutoPilot {

	private GroundHeadingControl groundHeadingControl;
	private RollDegsControl rollDegsControl;
	private YawDegsControl yawDegsControl;
	private PitchDegsControl pitchDegsControl;
	private static int writeCount = 0;

	/* stage control */
	boolean stage1 = true; /* gears leave ground */
	boolean stage2 = false; /* climb to safety height */
	boolean stage3 = false; /* keep on the same height */

	public TakingOff(Aeroplane aeroplane, Airport airport) {
		super(aeroplane, airport);
		groundHeadingControl = new GroundHeadingControl(0.2, 0.005, 10.0);
		rollDegsControl = new RollDegsControl(0.5, 0.0002, 0.0);
		yawDegsControl = new YawDegsControl(0.06, 0.0000, 0.0);
		pitchDegsControl = new PitchDegsControl(0.06, 0.0, 0.0);
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
					rudder = (float) groundHeadingControl.getResult(airport
							.getDirection()
							- aeroplane.getHeadingDeg());
					aileron = 0;
					elevator = 0;
					brakeParking = 0;
				}
			}

			if (stage2) {
				if (aeroplane.getAltitudeAglFt() > 600) {
					stage1 = false;
					stage2 = false;
					stage3 = true;
					System.out.println("Entered stage 3!");
				} else {
					throttle = (float) 1.0;
				
					rudder = (float) yawDegsControl.getResult(airport
							.getDirection()
							- aeroplane.getHeadingDeg());
					
					aileron = (float) rollDegsControl.getResult(0 - aeroplane
							.getRollDeg());

					
					elevator = (float) pitchDegsControl
							.getResult(8.0 - aeroplane.getPitchDeg());

					rudder += (float) 0.09;
					aileron += (float) 0.05;
					brakeParking = 1;
				}
			}

			if (stage3) {
				throttle = (float) 0.62;

				System.out.println("Heading error is: "
						+ (airport.getDirection() - aeroplane.getHeadingDeg()));
				rudder = (float) yawDegsControl.getResult(airport
						.getDirection()
						- aeroplane.getHeadingDeg());
				System.out.println("Rolling error is: "
						+ (0 - aeroplane.getRollDeg()));
				aileron = (float) rollDegsControl.getResult(0 - aeroplane
						.getRollDeg());
				System.out.println("Pitch Degree is: "
						+ aeroplane.getPitchDeg());
				elevator = (float) pitchDegsControl.getResult(0 - aeroplane
						.getPitchDeg());

				rudder += (float) 0.0075;
				aileron += (float) 0.006;
				brakeParking = 1;

			}

			writeCount++;
			if (writeCount > 15) {
				KMLFileWritter.writeToFile(aeroplane.getLatitude(), aeroplane.getLongitude());
				writeCount = 0;
			}
			sendCommand();

		}
	}
}
