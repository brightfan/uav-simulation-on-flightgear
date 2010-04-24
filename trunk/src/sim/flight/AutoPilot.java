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
	protected Aeroplane aeroplane;
	protected Airport airport;
	/* Constant Control Parameter */
	protected double navigationHeight = 600;

	/* flight control parameters */
	protected float throttle;
	protected float rudder;
	protected float aileron;
	protected float elevator;
	protected int brakeParking;

	/* network connection */
	private DatagramSocket clientSocket;
	InetAddress IPAddress;
	private int outPort;
	private byte[] sendBuffer = new byte[200];

	/* logger */
	protected static boolean isLogging = false;
	protected static int commandlineLogCounter = 0;
	protected static final int ONESECONDCOUNTER = 40;

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
		sendBuffer = new String(throttle + "\t" + rudder + "\t" + aileron
				+ "\t" + elevator + "\t" + brakeParking + "\n").getBytes();

		DatagramPacket sendPacket = new DatagramPacket(sendBuffer,
				sendBuffer.length, IPAddress, outPort);

		try {
			clientSocket.send(sendPacket);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		return true;
	}

	public void startAutoPilot() {
		if (isInitiated) {
			autoPilot();
		}
	}

	public void stopTransferring() {
		clientSocket.close();
	}

	public abstract AutoPilot autoPilot();

	public void logToCommandLine() {
		if (commandlineLogCounter == ONESECONDCOUNTER) {
			commandlineLogCounter = 0;
		}
		commandlineLogCounter++;
	}
}
