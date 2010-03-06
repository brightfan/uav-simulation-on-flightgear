package sim.utils;

public class Waypoint {
	private String id;
	private double latitude, longitude, approachRadius, height;
	
	public Waypoint() {
		; // nothing to do
	}
	
	public Waypoint(double latitude, double longitude) {
		this("Airport", latitude, longitude, 0, 0);
	}
	
	public Waypoint(String id, double latitude, double longitude, double approachRadius) {
		this(id, latitude, longitude, approachRadius, 0);
	}
	
	public Waypoint(String id, double latitude, 
					double longitude, double approachRadius, double height) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.approachRadius = approachRadius;
		this.height= height;
	}
	
	/* Please make sure to pass in an array with 5 or more elements */
	public static Waypoint parseWaypoint(double[] waypoint) {
		return new Waypoint(String.valueOf(waypoint[0]), waypoint[1], waypoint[2], waypoint[3], waypoint[4]);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longtitude) {
		this.longitude = longtitude;
	}

	public double getApproachRadius() {
		return approachRadius;
	}

	public void setApproachRadius(double approachRadius) {
		this.approachRadius = approachRadius;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		} else if (other == this) {
			return true;
		} else if ( !(other instanceof Waypoint)) {
			return false;
		} else {
			Waypoint waypoint = (Waypoint)other;
			return waypoint.getId().equals(id) &&
					waypoint.getLatitude() == latitude &&
					waypoint.getLongitude() == longitude &&
					waypoint.getApproachRadius() == approachRadius &&
					waypoint.getHeight() == height;
		}
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %f, %f, %f, %f]", id, latitude, longitude, approachRadius, height);
	}
}
