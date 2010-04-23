package sim.controller;

public class AirborneSpeedControl extends PIDBase {

	public AirborneSpeedControl(double KP, double KI, double KD) {
		super(KP, KI, KD);
	}
	
	@Override
	public double getResult(double error) {

		accumulateError(error);
		
		double result = KP * error + KI * accumulatedError;

		if (result > 1.0) {
			result = 1.0;
		}
		else if (result < 0.0) {
			result = 0.0;
		}
		
		return result;
	}

}
