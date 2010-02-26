package sim.flight;

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
		accumulateError(error);
		
		double result =  KP * error + KI * accumulatedError + KD * (currentError - previousError);
		
		result /= 10.0;
		System.out.println("P is : " + error);
		System.out.println("I is : " + accumulatedError);
		System.out.println("D is : " + (currentError - previousError));
		System.out.println("result is : " + result);
		
		if (result > 0.6) {
			result = 0.6;
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
