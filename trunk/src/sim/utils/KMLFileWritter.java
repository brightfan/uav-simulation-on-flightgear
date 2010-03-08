package sim.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class KMLFileWritter {
	public static String filePath = "/Users/Helicopter/Software/apache-tomcat-6.0.20/webapps/kml/test.kml";
	public static FileOutputStream fOutStream;

	public static void writeToFile(float latitude, float longitude) {

		//Kml kml = new Kml();
		
		//kml.createAndSetDocument().withName("Current Position").withOpen(
		//Boolean.TRUE).createAndSetCamera().withLatitude(latitude)
		//.withLongitude(longitude).withAltitude(altitude).withHeading(
		//heading).withTilt(tilt).withAltitudeMode( AltitudeMode.ABSOLUTE);
		
		//kml.createAndSetPlacemark().withName("Current Position").withOpen(
		//		Boolean.TRUE).createAndSetPoint().addToCoordinates(longitude,
		//		latitude);
		
		String kmlStr = "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">" +
		"<Document>" +
		"<name>UAV Tracking</name>" +
		"<open>1</open>" +
		"<Style id=\"highlightPlacemark\">" +
		"<IconStyle>" +
		//"<color>641400FF</color>" +
		"<Icon>" +
        "<href>http://maps.google.com/mapfiles/ms/icons/plane.png</href>" +
        "</Icon>" +
		"</IconStyle>" +
		"</Style>" +
		"<Placemark>" +
		"<name>C-172N</name>" +
		"<styleUrl>#highlightPlacemark</styleUrl>" +
		"<Point>" +
		"<coordinates>"+longitude+","+latitude+"</coordinates>" +
		"</Point>" +
		"</Placemark>" +
		"</Document>" +
		"</kml>";
	
		try {
			//kml.marshal(fOutStream);
			fOutStream = new FileOutputStream(filePath);
			fOutStream.write(kmlStr.getBytes());
			fOutStream.flush();
			fOutStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		double lat1 = 37.61354101;
		double lon1 = -122.3572386;
		int i = 0;
		
		while (true) {
			writeToFile((float) lat1, (float) lon1);
			Waypoint wp = GlideSlope.generateWaypointByDistanceAndBearing(lat1,
					lon1, 30, 298);
			lat1 = wp.getLatitude();
			lon1 = wp.getLongitude();
			System.out.println(i++);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
