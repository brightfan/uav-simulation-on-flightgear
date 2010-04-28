package sim.main;

import java.util.ArrayList;
import java.util.List;

import sim.utils.Waypoint;

public final class FlightCourse {

	public static List<Waypoint> getCourse() {
		
		List<Waypoint> flightCourse = new ArrayList<Waypoint>();
		flightCourse.add(new Waypoint("1", 37.66452681, -122.4748992, 250, 1000));
		flightCourse.add(new Waypoint("2", 37.61314357, -122.5164413, 250, 1000));
		flightCourse.add(new Waypoint("3", 37.52851499, -122.4395370, 250, 1800));
		flightCourse.add(new Waypoint("4", 37.52443079, -122.3228073, 250, 2000));
		flightCourse.add(new Waypoint("5", 37.57547470, -122.2916205, 300, 1000));
		return flightCourse;
		
	}
	
	/* Attention: this debug course is for initiated point at
	 *            -122.5006208, 37.67894147
	 */
	public static List<Waypoint> getDebugCourse() {
		List<Waypoint> flightCourse = new ArrayList<Waypoint>();
		
		flightCourse.add(new Waypoint("1", 37.6915856, -122.5306811, 250, 1000));
		flightCourse.add(new Waypoint("2", 37.653111385018036, -122.53386497497559, 250, 1000));
		flightCourse.add(new Waypoint("3", 37.645975852090025, -122.50863075256348, 250, 1000));
		flightCourse.add(new Waypoint("4", 37.67894147, -122.5006208, 250, 1000));
		
		return flightCourse;
	}
}
