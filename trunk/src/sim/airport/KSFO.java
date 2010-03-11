package sim.airport;

import sim.utils.Waypoint;

public class KSFO extends Airport {
	public KSFO() {
		this.runway = "1L";
		this.setRunwayDirection(298.0);
		this.setRunwayStartingPoint(new Waypoint("Airport", 37.61354101, -122.3572386));
		this.setRunwayEndingPoint(new Waypoint("Airport", 37.62863534, -122.3929095));
	}
}
