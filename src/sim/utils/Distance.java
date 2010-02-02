package sim.utils;

public class Distance {
	/*
	 * A = LAT1, B = LONG1 C = LAT2, D = LONG2 (all converted to radians:
	 * degree/57.29577951)
	 * 
	 * IF A = C AND B = D THEN DISTANCE = 0; ELSE IF
	 * [SIN(A)SIN(C)+COS(A)COS(C)COS(B-D)] > 1 THEN DISTANCE = 3963.1*ARCOS[1];
	 * ELSE DISTANCE = 3963.1*ARCOS[SIN(A)SIN(C)+COS(A)COS(C)COS(B-D)];
	 */
	public static double getDistance(double lat1, double long1, double lat2,
			double long2) {
		double radiansLat1 = Math.toRadians(lat1);
		double radiansLat2 = Math.toRadians(lat2);
		double radiansLong1 = Math.toRadians(long1);
		double radiansLong2 = Math.toRadians(long2);

		if ((radiansLat1 == radiansLat2) && (radiansLong1 == radiansLong2))
			return 0;

		if ((Math.sin(radiansLat1) * Math.sin(radiansLat2) + Math
				.cos(radiansLat1)
				* Math.cos(radiansLat2) * Math.cos(radiansLong1 - radiansLong2)) > 1) {

			return 6378.137 * Math.acos(1) * 1000;
		} else {
			return 6378.137 * Math.acos(Math.sin(radiansLat1)
					* Math.sin(radiansLat2) + Math.cos(radiansLat1)
					* Math.cos(radiansLat2)
					* Math.cos(radiansLong1 - radiansLong2)) * 1000;
		}
	}
}
