package sim.controller;


public class PitchDegsControl extends PIDBase{
	
	public PitchDegsControl(double KP, double KI, double KD) {
		super(KP, KI, KD);
	}
	
	public double getResult(double error, float derivation) {
		
		//double trimmedError = processError(error);
		//accumulateError(trimmedError);
		accumulateError(error);
		
		double result =  KP * error + KI * accumulatedError + KD * derivation;
		//double result = KP * error;
		
		//System.out.println("P is : " + error);
		//System.out.println("I is : " + accumulatedError);
		//System.out.println("result is : " + result + " ********");
		
		if (result > 0.4) {
			result = 0.4;
		}
		else if (result < -0.4) {
			result = - 0.4;
		}
		
		if (Math.abs(error) > 0.05)
			return result;
		else
			return 0;
	}
}
