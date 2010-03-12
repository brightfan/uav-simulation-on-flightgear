package sim.flight;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.controller.PitchDegsControl;
import sim.controller.RollDegsControl;
import sim.controller.YawDegsControl;
import sim.utils.KMLFileWritter;

public class Navigation extends AutoPilot {

	/* flight controller */
	private RollDegsControl rollDegsControl;
	private YawDegsControl yawDegsControl;
	private PitchDegsControl pitchDegsControl;

	private double navigationHeight = 600;

	public Navigation(Aeroplane aeroplane, Airport airport) {
		super(aeroplane, airport);
		rollDegsControl = new RollDegsControl(0.5, 0.0002, 0.0);
		yawDegsControl = new YawDegsControl(0.06, 0.0000, 0.0);
		pitchDegsControl = new PitchDegsControl(0.25, 0.0, 0.0);

		System.out.println("********** Entered Navigation Mode! ***********");
	}

	@Override
	public void autoPilot() {
		while (true) {
			aeroplane.readStatus();
			
			double currentHeight = aeroplane.getAltitudeFt();
			double desiredPitch;
			
			desiredPitch = (navigationHeight - currentHeight) / 500 * 2;

			throttle = (float) 0.55;

			// System.out.println("Heading error is: "
			// + (airport.getRunwayDirection() -
			// aeroplane.getHeadingDeg()));
			rudder = (float) yawDegsControl.getResult(airport
					.getRunwayDirection()
					- aeroplane.getHeadingDeg());
			// System.out.println("Rolling error is: "
			// + (0 - aeroplane.getRollDeg()));
			aileron = (float) rollDegsControl.getResult(0 - aeroplane
					.getRollDeg());
			// System.out.println("Pitch Degree is: "
			// + aeroplane.getPitchDeg());
			elevator = (float) pitchDegsControl.getResult(desiredPitch);

			rudder += (float) 0.0075;
			aileron += (float) 0.006;
			brakeParking = 1;

			System.out
					.println("current speed is: " + aeroplane.getAirSpeedKt());
			System.out.println("current height is: "
					+ aeroplane.getAltitudeFt());
			System.out.println("current elevator is: " + elevator);
			System.out.println("current pitch is: " + aeroplane.getPitchDeg());
			System.out.println("desired pitch is: " + desiredPitch);

			if (isLogging) {
				writeCount++;
				if (writeCount > 15) {
					KMLFileWritter.writeToFile(aeroplane.getLatitude(),
							aeroplane.getLongitude());
					writeCount = 0;
				}
			}

			/* Send Flight Control Command to Aircraft */
			sendCommand();
		}
	}

}
