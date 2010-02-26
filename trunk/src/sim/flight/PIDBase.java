package sim.flight;

public abstract class PIDBase {
	protected double KP;
	protected double KI;
	protected double KD;
	
	protected double accumulatedError;
	protected double currentError;
	protected double previousError;
	
	public PIDBase(double KP, double KI, double KD) {
		this.KP = KP;
		this.KI = KI;
		this.KD = KD;
		
		this.accumulatedError = 0;
		this.currentError = 0;
		this.previousError = 0;
	}
	
	protected void accumulateError(double error) {
		previousError = currentError;
		currentError = error;
		accumulatedError += currentError;
		
		if (accumulatedError == currentError) {
			previousError = currentError;
		}
	}
	
	public double getResult(double error) {
		
		accumulateError(error);	
		
		//System.out.println("P is : " + error);
		//System.out.println("I is : " + accumulatedError);
		//System.out.println("D is : " + (currentError - previousError));
		
		return KP * error + KI * accumulatedError + KD * (currentError - previousError);
	}
}
