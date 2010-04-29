package sim.controller;


public class YawDegsControl extends PIDBase {
	public static double AMP_COEFFICIENT = 2;

	public YawDegsControl(double KP, double KI, double KD) {
		super(KP, KI, KD);
	}

	@Override
	public double getResult(double error) {
		
		//double trimmedError = processError(error);
		//accumulateError(trimmedError);
		accumulateError(error);
		
		double result =  KP * error + KI * accumulatedError;
		//double result = KP * error;
		//double result = Math.sin(Math.toRadians(error));
		
		//result *= 2.0;
		//System.out.println("P is : " + error);
		//System.out.println("I is : " + accumulatedError);
		//System.out.println("D is : " + (currentError - previousError));
		//System.out.println("result is : " + result + " ********");
		
		if (result > 0.5) {
			result = 0.5;
		}
		else if (result < -0.6) {
			result = - 0.6;
		}
		
		if (Math.abs(error) > 2.0)
			return result;
		else
			return 0;
	}
}
