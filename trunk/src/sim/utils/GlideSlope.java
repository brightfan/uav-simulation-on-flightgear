package sim.utils;

import java.util.ArrayList;
import java.util.List;

/* 
 * Using WGS84 Ellipsoid Earth Model
 * Calculation based on Vincenty's formula
 * Author: Ye Fan
 */

public class GlideSlope {

	public static List<Waypoint> generateGlideSlopeWaypointList() {

		List<Waypoint> glideSlopeWaypointList = new ArrayList<Waypoint>();

		return glideSlopeWaypointList;
	}

	public static Waypoint generateWaypointByDistanceAndBearing(
			double latitude, double longitude, double distance, double bearing) {

		/* Convert Degrees to Radians */
		double lat1 = Math.toRadians(latitude);
		double lon1 = Math.toRadians(longitude);
		double brg = Math.toRadians(bearing);

		/*
		 * Calculate the destination point using Vicenty's formula. Shortened
		 * variable names are used.
		 */
		double a = Constants.majorAxisLengthEarthM;
		double b = Constants.minorAxisLengthEarthM;
		double flat = a / (a - b);
		double f = 1 / flat;
		double sb = Math.sin(brg);
		double cb = Math.cos(brg);
		double tu1 = (1 - f) * Math.tan(lat1);
		double cu1 = 1 / Math.sqrt(1 + tu1 * tu1);
		double su1 = tu1 * cu1;
		double s2 = Math.atan2(tu1, cb);
		double sa = cu1 * sb;
		double csa = 1 - sa * sa;
		double us = csa * (a * a - b * b) / b / b;
		double A = 1 + us / 16384
				* (4096 + us * (-768 + us * (320 - 175 * us)));
		double B = us / 1024 * (256 + us * (-128 + us * (74 - 47 * us)));
		double s1 = distance / b / A;
		double s1p = 2 * Math.PI;

		/* Loop through the following while condition is true */
		double cs1m = 0;
		double ss1 = 0;
		double cs1 = 0;
		double ds1 = 0;
		while (Math.abs(s1 - s1p) > 0.000000000001) {
			cs1m = Math.cos(2 * s2 + s1);
			ss1 = Math.sin(s1);
			cs1 = Math.cos(s1);
			ds1 = B
					* ss1
					* (cs1m + B
							/ 4
							* (cs1 * (-1 + 2 * cs1m * cs1m) - B / 6 * cs1m
									* (-3 + 4 * ss1 * ss1)
									* (-3 + 4 * cs1m * cs1m)));
			s1p = s1;
			s1 = distance / b / A + ds1;
		}

		/* Continue calculation after the loop */
		double t = su1 * ss1 - cu1 * cs1 * cb;
		double lat2 = Math.atan2(su1 * cs1 + cu1 * ss1 * cb, (1 - f)
				* Math.sqrt(sa * sa + t * t));
		double l2 = Math.atan2(ss1 * sb, cu1 * cs1 - su1 * ss1 * cb);
		double c = f / 16 * csa * (4 + f * (4 - 3 * csa));
		double l = l2 - (1 - c) * f * sa
				* (s1 + c * ss1 * (cs1m + c * cs1 * (-1 + 2 * cs1m * cs1m)));
		double d = Math.atan2(sa, -t);
		double finalBrg = d + 2 * Math.PI;
		double backBrg = d + Math.PI;
		double lon2 = lon1 + l;

		/* Convert lat2, lon2, finalBrg and backBrg to degrees */
		lat2 = Math.toDegrees(lat2);
		lon2 = Math.toDegrees(lon2);
		finalBrg = Math.toDegrees(finalBrg);
		backBrg = Math.toDegrees(backBrg);

		/* Trim output */
		if (lon2 > 180) {
			lon2 = lon2 - 360;
		} else if (lon2 < -180) {
			lon2 = lon2 + 360;
		}

		return new Waypoint("0", lat2, lon2, 5);
	}

	public static void main(String[] args) {
		Waypoint wayPoint = generateWaypointByDistanceAndBearing(37.61354101,
				-122.3572386, 200, 118);
		System.out.println(wayPoint);
	}
}
