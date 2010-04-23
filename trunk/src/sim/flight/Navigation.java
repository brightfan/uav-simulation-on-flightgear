package sim.flight;

import java.util.List;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.controller.AirborneSpeedControl;
import sim.controller.PitchDegsControl;
import sim.controller.RollDegsControl;
import sim.main.FlightCourse;
import sim.main.MainLoop;
import sim.utils.Direction;
import sim.utils.DirectionError;
import sim.utils.Distance;
import sim.utils.KMLFileWritter;
import sim.utils.Waypoint;

public class Navigation extends AutoPilot {

	/* flight controller */
	private RollDegsControl rollDegsControl;
	private PitchDegsControl pitchDegsControl;
	private AirborneSpeedControl airborneSpeedControl;

	private double navigationHeight = 600;

	private List<Waypoint> flightCourse;
	private int currentHeadingWaypointIndex;

	public Navigation(Aeroplane aeroplane, Airport airport) {

		super(aeroplane, airport);

		rollDegsControl = new RollDegsControl(0.0333, 0.0002, 0.0);
		// yawDegsControl = new YawDegsControl(0.06, 0.0000, 0.0);
		pitchDegsControl = new PitchDegsControl(0.1, 0.005, 0.01);
		airborneSpeedControl = new AirborneSpeedControl(0.03, 0.0001, 0.0);

		if (MainLoop.isDebugging == true) {
			flightCourse = FlightCourse.getDebugCourse();
		} else {
			flightCourse = FlightCourse.getCourse();
		}
		currentHeadingWaypointIndex = 0;

		System.out.println("********** Entered Navigation Mode! ***********");
	}

	@Override
	public void autoPilot() {
		while (true) {
			aeroplane.readStatus();

			/*
			 * rudder = (float) yawDegsControl.getResult(airport
			 * .getRunwayDirection() - aeroplane.getHeadingDeg());
			 *//* THIS IS WORKING!!!! */

			/* Whether Arrived Way Point? */
			double distance = Distance.getDistance(aeroplane.getLatitude(),
					                               aeroplane.getLongitude(),
					                               flightCourse.get(currentHeadingWaypointIndex).getLatitude(),
					                               flightCourse.get(currentHeadingWaypointIndex).getLongitude());
			if (distance < flightCourse.get(currentHeadingWaypointIndex)
					.getApproachRadius()) {
				/* Arrived Desired Way Point */
				System.out.println("i'm here");
				currentHeadingWaypointIndex++;

				if (MainLoop.isDebugging) {
					currentHeadingWaypointIndex %= flightCourse.size();
				}

				if (currentHeadingWaypointIndex == flightCourse.size()) {
					/* Go to Landing Process */
				}
			}

			double desiredHeadingDirection = Direction
					.getDirection(aeroplane.getLatitude(),
						          aeroplane.getLongitude(),
								  flightCourse.get(currentHeadingWaypointIndex).getLatitude(), 
								  flightCourse.get(currentHeadingWaypointIndex).getLongitude());

			double directionError = DirectionError.getError(aeroplane
					.getHeadingDeg(), desiredHeadingDirection);
			directionError = DirectionError.constrainError(directionError);

			double desiredAileron = Math.sin(Math.toRadians(directionError)) * 30;

			aileron = (float) rollDegsControl.getResult(desiredAileron
					- aeroplane.getRollDeg());

			double currentHeight = aeroplane.getAltitudeFt();
			double desiredPitch;
			desiredPitch = (1 / (Math.pow(1.05,
					-(navigationHeight - currentHeight)) + 1) - 0.5) * 60;

			elevator = (float) pitchDegsControl.getResult(aeroplane
					.getPitchDeg()
					- desiredPitch, aeroplane.getPitchRateDegps());

			throttle = (float) airborneSpeedControl.getResult(100 - aeroplane
					.getGroundSpeedKt());

			rudder = (float) 0;
			aileron += (float) 0.006;
			brakeParking = 1;

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
				/*
				 * System.out.println("Height Error: " + (navigationHeight -
				 * currentHeight)); System.out.println("Desired Pitch: " +
				 * desiredPitch); System.out.println("Elevator: " + elevator);
				 */

				/* FOR THROTTLE DEBUG */
				/*System.out.println("Gound Speed: "
						+ aeroplane.getGroundSpeedKt());*/
				
				/* FOR COURSE DEBUG*/
				System.out.println("Next Waypoint is the: " + currentHeadingWaypointIndex + "th");
				System.out.println("Current Heading: " + aeroplane.getHeadingDeg());
				System.out.println("Desired Heading: " + desiredHeadingDirection);
				System.out.println("Distance left: " + distance);
				
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
