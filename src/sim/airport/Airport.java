package sim.airport;

public abstract class Airport {
	public String runway;
	public float direction;
	public float latitude;
	public float longitude;
	
	public float getDirection() {
		return direction;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
}
