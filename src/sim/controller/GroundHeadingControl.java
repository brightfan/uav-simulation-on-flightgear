package sim.controller;


public class GroundHeadingControl extends PIDBase{
	
	public GroundHeadingControl(double KP, double KI, double KD) {
		super(KP, KI, KD);
	}

	private double processError(double error) {
		if(error > 0) {
			if(error > 180) {
				error = error - 360;
			} else {
				; // do nothing
			}
		} else {
			if (error < -180) {
				error = error + 360;
			} else {
				; //do nothing
			}
		}
		
		if (Math.abs(error) > 3) {
			return error;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public double getResult(double error) {
		double trimmedError = processError(error);
		
		accumulateError(trimmedError);
		
		//System.out.println("P is : " + error);
		//System.out.println("I is : " + accumulatedError);
		//System.out.println("D is : " + trimmedError);
		
		double result =  KP * error + KI * accumulatedError + KD * (currentError - previousError);
		
		return result/5;
	}
}
