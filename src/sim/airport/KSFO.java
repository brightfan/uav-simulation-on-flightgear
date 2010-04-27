package sim.airport;

import java.util.List;

import sim.utils.Waypoint;

public class KSFO extends Airport {
	public KSFO() {
		this.runway = "1L";
		this.setRunwayDirection(298.0);
		this.setRunwayStartingPoint(new Waypoint("Airport", 37.61354101, -122.3572386));
		this.setRunwayEndingPoint(new Waypoint("Airport", 37.6177566, -122.3672488));
		//this.setRunwayEndingPoint(new Waypoint("Airport", 37.62863534, -122.3929095));
		this.setLandingPointAltitude(0);
		this.setLandHeight(4.12);
	}
	
	public static void main(String [] args) {
		Airport airport = new KSFO();
		List<Waypoint> glideSlope = airport.getGlideSlope();
		for (Waypoint point: glideSlope)
			System.out.println(point);
	}
}
