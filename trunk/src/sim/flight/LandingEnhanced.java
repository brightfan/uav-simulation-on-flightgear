package sim.flight;

import java.util.List;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.controller.AirborneSpeedControl;
//import sim.controller.GroundHeadingControl;
import sim.controller.PitchDegsControl;
import sim.controller.RollDegsControl;
import sim.controller.YawDegsControl;
import sim.main.MainLoop;
import sim.utils.Constants;
import sim.utils.Constraint;
import sim.utils.Direction;
import sim.utils.DirectionError;
import sim.utils.Distance;
import sim.utils.KMLFileWritter;
import sim.utils.Waypoint;

public class LandingEnhanced extends AutoPilot {
	/* flight controller */
	// private GroundHeadingControl groundHeadingControl;
	private RollDegsControl rollDegsControl;
	private PitchDegsControl pitchDegsControl;
	private AirborneSpeedControl airborneSpeedControl;
	private YawDegsControl yawDegsControl;

	private List<Waypoint> glideSlopeWaypoints;
	private int currentGlideSlopeWPIndex;

	private boolean startingRollOut = false;
	private boolean hasRolledOut = false;
	
	private double previousDistance = 100000;

	public LandingEnhanced(Aeroplane aeroplane, Airport airport) {
		super(aeroplane, airport);
		// groundHeadingControl = new GroundHeadingControl(0.2, 0.005, 10.0);
		rollDegsControl = new RollDegsControl(0.0333, 0.0002, 0.0);
		yawDegsControl = new YawDegsControl(0.06, 0.00005, 0.0);
		pitchDegsControl = new PitchDegsControl(0.08, 0.001, 0.01);
		airborneSpeedControl = new AirborneSpeedControl(0.03, 0.0001, 0.0);

		glideSlopeWaypoints = airport.getGlideSlope();
		currentGlideSlopeWPIndex = 0;

		if (MainLoop.isDebugging) {
			navigationHeight = 1000;
			navigationSpeed = 100;
			glideSlopeWaypoints.add(0, new Waypoint("5", 37.5754747, -122.2916205, 300, 1000, 100));
		}

		System.out
				.println("********** Landing Module Takes Control ***********");
	}

	@Override
	public AutoPilot autoPilot() {
		while (true) {
			aeroplane.readStatus();
			/* Whether Arrived Way Point? */
			double distance = Distance.getDistance(aeroplane.getLatitude(),
					aeroplane.getLongitude(), glideSlopeWaypoints.get(
							currentGlideSlopeWPIndex).getLatitude(),
					glideSlopeWaypoints.get(currentGlideSlopeWPIndex)
							.getLongitude());
			if (previousDistance < distance) {
				currentGlideSlopeWPIndex++;
				previousDistance = 100000;
			}
			else {
				previousDistance = distance;
			}
			if (distance < glideSlopeWaypoints.get(currentGlideSlopeWPIndex)
					.getApproachRadius()) {
				/* Arrived Desired Way Point */
				currentGlideSlopeWPIndex++;

				if ((glideSlopeWaypoints.size() - currentGlideSlopeWPIndex) >= 1) {
					navigationHeight = glideSlopeWaypoints.get(
							currentGlideSlopeWPIndex).getHeight();
				} else {
					startingRollOut = true;
				}

				if ((glideSlopeWaypoints.size() - currentGlideSlopeWPIndex) == 8) {
					// pitchDegsControl = new PitchDegsControl(0.1, 0.005,
					// 0.01);
				}

				navigationSpeed = glideSlopeWaypoints.get(
						currentGlideSlopeWPIndex).getSpeedLimit();
			}

			double desiredHeadingDirection = Direction.getDirection(aeroplane
					.getLatitude(), aeroplane.getLongitude(),
					glideSlopeWaypoints.get(currentGlideSlopeWPIndex)
							.getLatitude(), glideSlopeWaypoints.get(
							currentGlideSlopeWPIndex).getLongitude());

			if (currentGlideSlopeWPIndex <= 6) {
				double directionError = DirectionError.getError(aeroplane
						.getHeadingDeg(), desiredHeadingDirection);
				directionError = DirectionError.constrainError(directionError);

				double desiredAileron = Math
						.sin(Math.toRadians(directionError)) * 30;

				aileron = (float) rollDegsControl.getResult(desiredAileron
						- aeroplane.getRollDeg());
			} else {
				aileron = (float) rollDegsControl.getResult(0 - aeroplane
						.getRollDeg());
				rudder = (float) yawDegsControl
						.getResult(desiredHeadingDirection
								- aeroplane.getHeadingDeg());
			}

			double currentHeight = aeroplane.getAltitudeFt();
			double desiredPitch = 0;
			double dropingRate = aeroplane.getVerticalSpeedFps(); /* Unit is Feet Per Second */
			double distanceToRollOut = 0;

			if (!startingRollOut) {
				distanceToRollOut = Distance.getDistance(airport.getRunwayStartingPoint().getLatitude(),
														airport.getRunwayStartingPoint().getLongitude(),
														aeroplane.getLatitude(),
														aeroplane.getLongitude());
				double desiredHeight = (distanceToRollOut * Airport.GLIDESLOPETANGENT / Constants.FOOTTOMETER + airport.getLandHeight());
				double desiredSpeed = 70 + 35.0/3000 * distanceToRollOut;
				if (desiredHeight > 1000) {
					; //navigation height remains
				}
				else {
					navigationHeight = desiredHeight;
				}
				if (desiredSpeed> 100) {
					; //navigation speed remains
				}
				else {
					navigationSpeed = desiredSpeed;
				}
				desiredPitch = (1 / (Math.pow(1.05,
						-(navigationHeight - currentHeight)) + 1) - 0.5) * 60;

				if (currentGlideSlopeWPIndex <= 3) {
					desiredPitch = Constraint.constraint(desiredPitch, 4, -4);
				} else {
					desiredPitch = Constraint.constraint(desiredPitch, 4, -4);
				}
				elevator = (float) pitchDegsControl.getResult(aeroplane
						.getPitchDeg()
						- desiredPitch, aeroplane.getPitchRateDegps());

				elevator = (float) Constraint.constraint(elevator, 0.4, -0.4);
			} else {
				//desiredPitch = 4.0;
				desiredPitch = 0.5 * (-6.0 - dropingRate);
				if (desiredPitch > 4.0) {
					desiredPitch = 4.0;
				}
				elevator = (float) pitchDegsControl.getResult(aeroplane
						.getPitchDeg()
						- desiredPitch, aeroplane.getPitchRateDegps());

				elevator = (float) Constraint.constraint(elevator, 0.4, -0.4);
			}
			
			if ((currentGlideSlopeWPIndex > glideSlopeWaypoints.size() - 3) && (Math.abs(dropingRate) < 1) && (!hasRolledOut)) {
				hasRolledOut = true;
			}

			throttle = (float) airborneSpeedControl.getResult(navigationSpeed
					- aeroplane.getGroundSpeedKt());

			// rudder = (float) 0;
			//aileron += (float) 0.006;
			if (hasRolledOut) {
				brakeParking = 1;
			}
			else {
				brakeParking = 0;
			}

			logToCommandLine();

			if (commandlineLogCounter == ONESECONDCOUNTER) {
				/* FOR AILERON DEBUG */
				/*
				 * System.out.println("Direction Error: " + directionError);
				 * System.out.println("DesiredAileron: " + desiredAileron);
				 * System.out.println("Current roll degree is: " +
				 * aeroplane.getRollDeg());
				 */

				/* FOR ELEVATOR DEBUG */

				System.out.println("Height Error: "
						+ (navigationHeight - currentHeight));
				System.out.println("Desired Pitch: " + desiredPitch);
				System.out.println("Current Pitch: " + aeroplane.getPitchDeg());
				System.out.println("Elevator: " + elevator);

				/* FOR THROTTLE DEBUG */

				System.out.println("Gound Speed: "
						+ aeroplane.getGroundSpeedKt());

				/* FOR COURSE DEBUG */
				System.out.println("Next Glide Slope Waypoint is the: "
						+ currentGlideSlopeWPIndex + "th");
				System.out.println("Desired Height: " + navigationHeight);
				System.out.println("Current Heading: "
						+ aeroplane.getHeadingDeg());
				System.out.println("Desired Heading: "
						+ desiredHeadingDirection);
				System.out.println("Distance left: " + distance);
				System.out.println("Droping rate is: " + dropingRate);

				if (isLogging) {
					KMLFileWritter.writeToFile(aeroplane.getLatitude(),
							aeroplane.getLongitude());
				}
			}

			/* Send Flight Control Command to Aircraft */
			sendCommand();
		}
	}

}
