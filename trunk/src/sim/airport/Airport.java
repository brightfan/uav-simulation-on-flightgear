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
	private static double GLIDESLOPEINDEX [] = {3000, 2500, 2100, 1700, 1500, 1300, 1100, 900, 700, 500, 400, 300, 200, 100, 50};
	private static double GLIDESLOPETANGENT = 0.09;
	private static double GLIDESLOPEPOINTRAIUSCONST = 1.0 / 18;
	
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
		double prevDistance = 0;
		double dDistance = 0;
		
		for (double distance: GLIDESLOPEINDEX) {
			dDistance = distance - prevDistance;
			Waypoint waypoint = GlideSlopeUtils.generateWaypointByDistanceAndBearing(latitude, longitude, distance, bearing);
			waypoint.setHeight(distance * GLIDESLOPETANGENT);
			waypoint.setApproachRadius(dDistance * GLIDESLOPEPOINTRAIUSCONST);
			prevDistance = distance;
			
			glideSlope.add(waypoint);
		}
		
		glideSlope.add(new Waypoint("Roll_Out", latitude, longitude, 2));
		glideSlope.add(new Waypoint("Runway_End", runwayEndingPoint.getLatitude(), runwayEndingPoint.getLongitude(), 2));
	
		return glideSlope;
	}
}
