package sim.utils;

public class Constraint {
	public static double constraint(double value, double upperLimit, double bottomLimit) {
		if (value > upperLimit) {
			return upperLimit;
		}
		else if (value < bottomLimit) {
			return bottomLimit;
		}
		else {
			return value;
		}
	}
}
