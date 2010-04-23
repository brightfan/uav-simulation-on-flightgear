package sim.utils;

public class Direction {
	public static double getDirection(double lat1, double long1, double lat2, double long2) {
		double direction;
		double theta;
		
		theta = (lat1 + lat2) / 2;
		theta = Math.toRadians(theta);
		
		if (lat1 == lat2) {
			if (long2 > long1)
				direction = 90;
			else
				direction = 270;
		}
		else {
			direction = Math.toDegrees(Math.atan((long2 - long1) * Math.cos(theta) /(lat2 - lat1)));
		}
		
		if (lat1 > lat2) {
			direction = direction + 180;
		}
		
		if (direction < 0) {
			direction = 360 + direction;
		}
	
		return direction;
	}
	
	public static void main(String [] args) {
		System.out.println(getDirection(37.67894147, -122.5006208, 37.68032205, -122.50828743));
	}
}
