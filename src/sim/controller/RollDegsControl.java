package sim.controller;


public class RollDegsControl extends PIDBase{

	public RollDegsControl(double KP, double KI, double KD) {
		super(KP, KI, KD);	
	}
	
	/*private double processError(double error) {
		if (Math.abs(error) > 3) {
			return error;
		}
		else {
			return 0;
		}
	}*/
	
	@Override
	public double getResult(double error) {
		
		//double trimmedError = processError(error);
		//accumulateError(trimmedError);
		//accumulateError(error);
		
		//double result =  KP * error + KI * accumulatedError + KD * (currentError - previousError);
		//double result = KP * error;
		double result = Math.sin(Math.toRadians(error));
		
		result *= 3.0;
		
		if (result > 0.5) {
			result = 0.5;
		}
		else if (result < -0.6) {
			result = - 0.6;
		}
		
		if (Math.abs(error) > 1.5)
			return result;
		else
			return 0;
	}
}
