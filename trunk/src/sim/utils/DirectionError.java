package sim.utils;

public class DirectionError {
	
	public static double getError(double myDirection, double desiredDirection) {
		
		double directionError = desiredDirection - myDirection;
		
		if (directionError < 0) {
			if (directionError < -180) {
				directionError += 360;
			}
		} else {
			if (directionError > 180) {
				directionError -= 360;
			}
		}
		
		return directionError;
	}
	
	public static double constrainError(double directionError) {
		
		if (directionError > 90) {
			directionError = 90;
		}
		else if (directionError < -90) {
			directionError = -90;
		}
		
		return directionError;
	}
}
