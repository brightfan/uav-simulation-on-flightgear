package sim.airport;

import java.util.ArrayList;
import java.util.List;

import sim.utils.GlideSlopeUtils;
import sim.utils.Waypoint;

public abstract class Airport {
	public String runway;
	private double runwayDirection;
	private Waypoint runwayStartingPoint;
	private Waypoint runwayEndingPoint;
	private double landingPointAltitude;
	private static double GLIDESLOPEINDEX [] = {50, 100, 200, 300, 400, 500, 700, 900, 1100, 1300, 1500, 1700, 2100, 2500, 3000};
	private static double GLIDESLOPETANGENT = 0.09;
	private static double GLIDESLOPEPOINTRAIUSCONST = 1.0/15;
	
	public void setRunwayDirection(double direction) {
		this.runwayDirection = direction;
	}
	
	public double getRunwayDirection() {
		return runwayDirection;
	}

	public Waypoint getRunwayStartingPoint() {
		return runwayStartingPoint;
	}

	public void setRunwayStartingPoint(Waypoint landingPoint) {
		this.runwayStartingPoint = landingPoint;
	}

	public Waypoint getRunwayEndingPoint() {
		return runwayEndingPoint;
	}

	public void setRunwayEndingPoint(Waypoint takingoffPoint) {
		this.runwayEndingPoint = takingoffPoint;
	}
	
	public double getLandingPointAltitude() {
		return landingPointAltitude;
	}

	public void setLandingPointAltitude(double landingPointAltitude) {
		this.landingPointAltitude = landingPointAltitude;
	}

	public List<Waypoint> getGlideSlope() {
		List<Waypoint> glideSlope = new ArrayList<Waypoint>();
		
		double bearing = runwayDirection - 180;
		
		if (bearing < 0) {
			bearing += 360;
		}
		
		double latitude = runwayStartingPoint.getLatitude();
		double longitude = runwayStartingPoint.getLongitude();
		
		for (double distance: GLIDESLOPEINDEX) {
			Waypoint waypoint = GlideSlopeUtils.generateWaypointByDistanceAndBearing(latitude, longitude, distance, bearing);
			waypoint.setHeight(distance * GLIDESLOPETANGENT);
			waypoint.setApproachRadius(distance * GLIDESLOPEPOINTRAIUSCONST);
			
			glideSlope.add(waypoint);
		}
		
		return glideSlope;
	}
}
