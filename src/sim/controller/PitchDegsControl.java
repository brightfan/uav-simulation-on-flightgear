package sim.controller;


public class PitchDegsControl extends PIDBase{
	
	public PitchDegsControl(double KP, double KI, double KD) {
		super(KP, KI, KD);
	}
	
	@Override
	public double getResult(double error) {
		
		//double trimmedError = processError(error);
		//accumulateError(trimmedError);
		accumulateError(error);
		
		double result =  KP * error;// + KI * accumulatedError;
		//double result = KP * error;
		
		//System.out.println("P is : " + error);
		//System.out.println("I is : " + accumulatedError);
		//System.out.println("result is : " + result + " ********");
		
		if (result > 0.5) {
			result = 0.5;
		}
		else if (result < -0.5) {
			result = - 0.5;
		}
		
		if (Math.abs(error) > 0.05)
			return - result;
		else
			return 0;
	}
}
