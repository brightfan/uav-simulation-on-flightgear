package sim.controller;

public class AirborneHeadingControl extends PIDBase {

	public AirborneHeadingControl(double KP, double KI, double KD) {
		super(KP, KI, 0);
	}
	
	public AirborneHeadingControl() {
		super(1.0/100, 1.0/4000, 0);
	}

	private double processError(double error) {

		if (error > 0) {
			if (error > 180) {
				error = error - 360;
			} else {
				; // do nothing
			}
		} else {
			if (error < -180) {
				error = error + 360;
			} else {
				; // do nothing
			}
		}
		return error;
		/*if (Math.abs(error) > 5) {
			return error;
		}
		else {
			return 0;
		}*/
	}

	@Override
	public double getResult(double error) {

		double trimmedError = processError(error);

		accumulateError(trimmedError);
		
		/*if (trimmedError > 90) {
			trimmedError = 90;
		}
		else if (trimmedError < -90) {
			trimmedError = -90;
		}*/
		
		double result = KP * trimmedError + KI * accumulatedError;
		
		//double result = KP * Math.sin(Math.toRadians(error));

		if (result > 0.6) {
			result = 0.6;
		}
		else if (result < -0.6) {
			result = -0.6;
		}
		
		System.out.println("result is: " + result);
		
		return result;
	}

}
