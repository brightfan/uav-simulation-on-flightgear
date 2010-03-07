package sim.utils;

import java.io.File;
import java.io.FileNotFoundException;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Kml;

public class KMLFileWritter {
	public static String filePath = "/Users/Helicopter/Desktop/test.kml";

	public static void writeToFile(float latitude, float longitude,
			float altitude, float heading, float tilt) {

		Kml kml = new Kml();
		kml.createAndSetDocument().withName("Current Position").withOpen(
				Boolean.TRUE).createAndSetCamera().withLatitude(latitude)
				.withLongitude(longitude).withAltitude(altitude).withHeading(
						heading).withTilt(tilt).withAltitudeMode(
						AltitudeMode.ABSOLUTE);
		try {
			kml.marshal(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		writeToFile((float) 37.61354101, (float) -122.3572386, (float) 100,
				(float) 298, (float) 90);
	}
}
