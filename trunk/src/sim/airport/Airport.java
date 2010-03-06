package sim.airport;

import sim.utils.Waypoint;

public abstract class Airport {
	public String runway;
	private double direction;
	private Waypoint landingPoint;
	private Waypoint takingoffPoint;
	
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public double getDirection() {
		return direction;
	}

	public Waypoint getLandingPoint() {
		return landingPoint;
	}

	public void setLandingPoint(Waypoint landingPoint) {
		this.landingPoint = landingPoint;
	}

	public Waypoint getTakingoffPoint() {
		return takingoffPoint;
	}

	public void setTakingoffPoint(Waypoint takingoffPoint) {
		this.takingoffPoint = takingoffPoint;
	}
	
	
}
