package sim.flight;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;
import sim.controller.AirborneSpeedControl;
import sim.controller.PitchDegsControl;
import sim.controller.RollDegsControl;
import sim.controller.YawDegsControl;
import sim.utils.KMLFileWritter;

public class Navigation extends AutoPilot {

	/* flight controller */
	private RollDegsControl rollDegsControl;
	//private YawDegsControl yawDegsControl;
	private PitchDegsControl pitchDegsControl;
	private AirborneSpeedControl airborneSpeedControl;

	private double navigationHeight = 600;

	public Navigation(Aeroplane aeroplane, Airport airport) {

		super(aeroplane, airport);

		rollDegsControl = new RollDegsControl(0.0333, 0.0002, 0.0);
		//yawDegsControl = new YawDegsControl(0.06, 0.0000, 0.0);
		pitchDegsControl = new PitchDegsControl(0.1, 0.005, 0.01);
		airborneSpeedControl = new AirborneSpeedControl(0.03, 0.0001, 0.0);

		System.out.println("********** Entered Navigation Mode! ***********");
	}

	@Override
	public void autoPilot() {
		while (true) {
			aeroplane.readStatus();

			double currentHeight = aeroplane.getAltitudeFt();
			double desiredPitch;

			desiredPitch = (1 / (Math.pow(1.05, -(navigationHeight - currentHeight)) + 1) - 0.5) * 60;

			// throttle = (float) 0.55;
			throttle = (float) airborneSpeedControl.getResult(100 - aeroplane
					.getGroundSpeedKt());

			/*rudder = (float) yawDegsControl.getResult(airport
					.getRunwayDirection()
					- aeroplane.getHeadingDeg());*/   /* THIS IS WORKING!!!! */
		
			double directionError = airport.getRunwayDirection()
									- aeroplane.getHeadingDeg();
			if (directionError < 0) {
				if (directionError < -180) {
					directionError += 360;
				}
			} else {
				if (directionError > 180) {
					directionError -= 360;
				}
			}
			
			if (directionError > 90) {
				directionError = 90;
			}
			else if (directionError < -90) {
				directionError = -90;
			}
				
			double desiredAileron = Math.sin(Math.toRadians(directionError)) * (20);
			
			aileron = (float) rollDegsControl.getResult(desiredAileron - aeroplane
					.getRollDeg());
			
			elevator = (float) pitchDegsControl.getResult(aeroplane
					.getPitchDeg()
					- desiredPitch, aeroplane.getPitchRateDegps());

			//rudder += (float) 0.0075;
			rudder = (float) 0;
			aileron += (float) 0.006;
			brakeParking = 1;

			logToCommandLine();
			
			if (commandlineLogCounter == ONESECONDCOUNTER) {
				/* FOR AILERON DEBUG */
				/*System.out.println("Direction Error: " + directionError);
				System.out.println("DesiredAileron: " + desiredAileron);
				System.out.println("Current roll degree is: " + aeroplane.getRollDeg());*/
				
				/* FOR ELEVATOR DEBUG */
				System.out.println("Height Error: " + (navigationHeight - currentHeight));
				System.out.println("Desired Pitch: " + desiredPitch);
				System.out.println("Elevator: " + elevator);
			}
			
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
