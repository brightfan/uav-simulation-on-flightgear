package sim.flight;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import sim.aircraft.Aeroplane;
import sim.airport.Airport;

public abstract class AutoPilot {
	private Aeroplane aeroplane;
	private Airport airport;

	/* flight control parameters */
	private float throttle;
	private float rudder;
	private float aileron;
	private float elevator;
	private int brakeParking;

	/* network connection */
	private DatagramSocket clientSocket;
	InetAddress IPAddress;
	private int outPort;
	private byte[] sendBuffer = new byte[200];

	private boolean isInitiated;

	public AutoPilot(Aeroplane aeroplane, Airport airport) {
		this.aeroplane = aeroplane;
		this.airport = airport;
		this.isInitiated = false;
	}

	public boolean initiateConnection(byte[] outRawIP, int outPort) {
		try {
			clientSocket = new DatagramSocket();
			this.outPort = outPort;
			IPAddress = InetAddress.getByAddress(outRawIP);
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}

		isInitiated = true;
		return true;
	}

	public boolean sendCommand() {
		sendBuffer = new String(throttle+"\t"+rudder+"\t"+aileron+"\t"+elevator+"\t"+brakeParking+"\n").getBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendBuffer,
				sendBuffer.length, IPAddress, outPort);

		try {
			clientSocket.send(sendPacket);
			Thread.sleep(100);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void startAutoPilot() {
		if (isInitiated) {
			autoPilot();
		}
	}
	
	public abstract void autoPilot();
}
