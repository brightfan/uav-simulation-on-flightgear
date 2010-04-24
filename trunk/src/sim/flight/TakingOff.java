package sim.flight;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.controller.AirborneSpeedControl;
import sim.controller.GroundHeadingControl;
import sim.controller.PitchDegsControl;
import sim.controller.RollDegsControl;
import sim.controller.YawDegsControl;
import sim.utils.DirectionError;
import sim.utils.KMLFileWritter;

public class TakingOff extends AutoPilot {

	/* flight controls */
	private GroundHeadingControl groundHeadingControl;
	private AirborneSpeedControl airborneSpeedControl;
	private RollDegsControl rollDegsControl;
	private YawDegsControl yawDegsControl;
	private PitchDegsControl pitchDegsControl;

	/* stage control */
	private boolean stage1 = true; /* gears leave ground */
	private boolean stage2 = false; /* climb to safety height */
	private boolean stage3 = false; /* keep on the same height */

	/* local counter */
	private int giveUpControlCounter;

	/* local counter maximum value */
	private static int COUNTERLIMITATION = 15; /*
												 * Aeroplane is assumed to
												 * arrive a stable condition
												 * after 15 second
												 */

	public TakingOff(Aeroplane aeroplane, Airport airport) {
		super(aeroplane, airport);
		groundHeadingControl = new GroundHeadingControl(0.2, 0.005, 10.0);
		rollDegsControl = new RollDegsControl(0.5, 0.0002, 0.0);
		yawDegsControl = new YawDegsControl(0.06, 0.0000, 0.0);
		pitchDegsControl = new PitchDegsControl(0.06, 0.0, 0.0);

		giveUpControlCounter = 0;
	}

	@Override
	public AutoPilot autoPilot() {
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
							.getRunwayDirection()
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
					airborneSpeedControl = new AirborneSpeedControl(0.03,
							0.0001, 0.0);
					rollDegsControl = new RollDegsControl(0.0333, 0.0002, 0.0);
					System.out.println("Entered stage 3!");
				} else {
					throttle = (float) 1.0;

					rudder = (float) yawDegsControl.getResult(airport
							.getRunwayDirection()
							- aeroplane.getHeadingDeg());

					aileron = (float) rollDegsControl.getResult(0 - aeroplane
							.getRollDeg());

					elevator = (float) pitchDegsControl.getResult(aeroplane
							.getPitchDeg() - 8.0);

					rudder += (float) 0.09;
					aileron += (float) 0.05;
					brakeParking = 1;
				}
			}

			if (stage3) {
				double directionError = DirectionError.getError(aeroplane
						.getHeadingDeg(), airport.getRunwayDirection());
				directionError = DirectionError.constrainError(directionError);

				double desiredAileron = Math
						.sin(Math.toRadians(directionError)) * 30;

				aileron = (float) rollDegsControl.getResult(desiredAileron
						- aeroplane.getRollDeg());

				double currentHeight = aeroplane.getAltitudeFt();
				double desiredPitch;
				desiredPitch = (1 / (Math.pow(1.05,
						-(navigationHeight - currentHeight)) + 1) - 0.5) * 60;

				elevator = (float) pitchDegsControl.getResult(aeroplane
						.getPitchDeg()
						- desiredPitch, aeroplane.getPitchRateDegps());

				throttle = (float) airborneSpeedControl
						.getResult(100 - aeroplane.getGroundSpeedKt());

				rudder = (float) 0;
				aileron += (float) 0.006;
				brakeParking = 1;

			}

			logToCommandLine();

			if (commandlineLogCounter == ONESECONDCOUNTER) {
				if (stage3) {
					giveUpControlCounter++;
				}

				System.out.println("current speed is: "
						+ aeroplane.getAirSpeedKt());
				System.out.println("current height is: "
						+ aeroplane.getAltitudeFt());
				System.out.println("current elevator is: " + elevator);
				System.out.println("current pitch is: "
						+ aeroplane.getPitchDeg());

				if (isLogging) {
					KMLFileWritter.writeToFile(aeroplane.getLatitude(),
							aeroplane.getLongitude());
				}
			}

			if (giveUpControlCounter >= COUNTERLIMITATION) {
				System.out.println("Take off module hands off control!");
				stopTransferring();
				return new Navigation(this.aeroplane, this.airport,
						this.rollDegsControl, this.pitchDegsControl,
						this.airborneSpeedControl);
			}

			/* Send Flight Control Command to Aircraft */
			sendCommand();
		}

	}
}
