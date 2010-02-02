package sim.aircraft;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

public class Aeroplane {
	private float rollDeg;
	private float pitchDeg;
	private float headingDeg;
	private float rollRateDegps;
	private float pitchRateDegps;
	private float yawRateDegps;
	private float xAccelFps;
	private float yAccelFps;
	private float zAccelFps;
	private float airSpeedKt;
	private float groundSpeedKt;
	private float verticalSpeedFps;
	private float altitudeAglFt;
	private float altitudeFt;
	private float latitude;
	private float longitude;
	
	/* network connection used*/
	private DatagramSocket serverSocket;
	private byte[] statusBuffer = new byte[1024];
	
	public Aeroplane(int port) {
		try {
			serverSocket = new DatagramSocket(port);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public boolean readStatus() {
		DatagramPacket receivePacket = new DatagramPacket(statusBuffer, statusBuffer.length);

		try {
			serverSocket.receive(receivePacket);
			String status = new String(receivePacket.getData());
			Scanner scanner = new Scanner(status);
			float [] statusList = new float[16];
			
			for (int i=0; i < 16;i++) {
				if (!scanner.hasNext()) {
					return false;
				}
				
				statusList[i] = scanner.nextFloat(); 
			}
			
			rollDeg = statusList[0];
			pitchDeg = statusList[1];
			headingDeg = statusList[2];
			rollRateDegps = statusList[3];
			pitchRateDegps = statusList[4];
			yawRateDegps = statusList[5];
			xAccelFps = statusList[6];
			yAccelFps = statusList[7];
			zAccelFps = statusList[8];
			airSpeedKt = statusList[9];
			groundSpeedKt = statusList[10];
			verticalSpeedFps = statusList[11];
			altitudeAglFt = statusList[12];
			altitudeFt = statusList[13];
			latitude = statusList[14];
			longitude = statusList[15];		
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public float getRollDeg() {
		return rollDeg;
	}

	public void setRollDeg(float rollDeg) {
		this.rollDeg = rollDeg;
	}

	public float getPitchDeg() {
		return pitchDeg;
	}

	public void setPitchDeg(float pitchDeg) {
		this.pitchDeg = pitchDeg;
	}

	public float getHeadingDeg() {
		return headingDeg;
	}

	public void setHeadingDeg(float headingDeg) {
		this.headingDeg = headingDeg;
	}

	public float getRollRateDegps() {
		return rollRateDegps;
	}

	public void setRollRateDegps(float rollRateDegps) {
		this.rollRateDegps = rollRateDegps;
	}

	public float getPitchRateDegps() {
		return pitchRateDegps;
	}

	public void setPitchRateDegps(float pitchRateDegps) {
		this.pitchRateDegps = pitchRateDegps;
	}

	public float getYawRateDegps() {
		return yawRateDegps;
	}

	public void setYawRateDegps(float yawRateDegps) {
		this.yawRateDegps = yawRateDegps;
	}

	public float getxAccelFps() {
		return xAccelFps;
	}

	public void setxAccelFps(float xAccelFps) {
		this.xAccelFps = xAccelFps;
	}

	public float getyAccelFps() {
		return yAccelFps;
	}

	public void setyAccelFps(float yAccelFps) {
		this.yAccelFps = yAccelFps;
	}

	public float getzAccelFps() {
		return zAccelFps;
	}

	public void setzAccelFps(float zAccelFps) {
		this.zAccelFps = zAccelFps;
	}

	public float getAirSpeedKt() {
		return airSpeedKt;
	}

	public void setAirSpeedKt(float airSpeedKt) {
		this.airSpeedKt = airSpeedKt;
	}

	public float getGroundSpeedKt() {
		return groundSpeedKt;
	}

	public void setGroundSpeedKt(float groundSpeedKt) {
		this.groundSpeedKt = groundSpeedKt;
	}

	public float getVerticalSpeedFps() {
		return verticalSpeedFps;
	}

	public void setVerticalSpeedFps(float verticalSpeedFps) {
		this.verticalSpeedFps = verticalSpeedFps;
	}

	public float getAltitudeAglFt() {
		return altitudeAglFt;
	}

	public void setAltitudeAglFt(float altitudeAglFt) {
		this.altitudeAglFt = altitudeAglFt;
	}

	public float getAltitudeFt() {
		return altitudeFt;
	}

	public void setAltitudeFt(float altitudeFt) {
		this.altitudeFt = altitudeFt;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
}
