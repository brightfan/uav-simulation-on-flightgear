package sim.flight;

public class PitchDegsControl extends PIDBase{
	
	public PitchDegsControl(double KP, double KI, double KD) {
		super(KP, KI, KD);
	}
	
	@Override
	public double getResult(double error) {
		
		//double trimmedError = processError(error);
		//accumulateError(trimmedError);
		accumulateError(error);
		
		//double result =  KP * error + KI * accumulatedError + KD * (currentError - previousError);
		//double result = KP * error;
		double result = Math.sin(Math.toRadians(error));
		
		result *= 6.0;
		
		if (result > 0.5) {
			result = 0.5;
		}
		else if (result < -0.6) {
			result = - 0.6;
		}
		
		if (Math.abs(error) > 1.5)
			return - result;
		else
			return 0;
	}
}
