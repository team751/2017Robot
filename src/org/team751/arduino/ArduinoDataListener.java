package org.team751.arduino;

import java.io.IOException;
import java.net.*;

public class ArduinoDataListener implements Runnable {
	private boolean isRunning = true;
	private final int port;
	private double heading; // axes of operation
	// private double distance;
	// private double velocity;

	private arduinoData leftSideData = new arduinoData();
	private arduinoData rightSideData = new arduinoData();

	public ArduinoDataListener(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		System.out.println("Is going");

		if (clientSocket == null)
			return;

		byte[] receiveData = new byte[1024];

		while (clientSocket.isBound() && isRunning) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(),
					receivePacket.getLength());
			System.out.println("Got " + modifiedSentence);
			// form: [heading,velocity,distance]
			modifiedSentence = modifiedSentence.replaceAll("\\[|\\]", ""); // remove the hard brackets on either side to
																			// prevent substrings because easier
			String[] stuff = modifiedSentence.split(",");

			// for testing
			System.out.println(stuff[0]);
			// stuff[0] should return a string that either says "left" or "right"
			heading = Double.parseDouble(stuff[1]);
			if (stuff[0] == "left") {
				leftSideData.setVelocity(Double.parseDouble(stuff[2]));
				leftSideData.setDistance(Double.parseDouble(stuff[3]));
			} else if (stuff[1] == "right") {
				rightSideData.setVelocity(Double.parseDouble(stuff[2]));
				rightSideData.setDistance(Double.parseDouble(stuff[3]));
			} else
				System.out.println("Input from the arduino is not a side");
		}
		clientSocket.close();
	}

	// public class leftSideData extends arduinoData{
	// public leftSideData(double heading, double velocity, double distance) {
	// super(heading, velocity, distance);
	// }
	// }
	//
	// public class rightSideData extends arduinoData{
	// public rightSideData(double heading, double velocity, double distance) {
	// super(heading, velocity, distance);
	// }
	// }
	private class arduinoData {
		private double velocity;
		private double distance;

		public arduinoData() {
			this(0, 0);
		}

		public arduinoData(double velocity, double distance) {
			this.velocity = velocity;
			this.distance = distance;
		}

		public double getVelocity() {
			return velocity;
		}

		public double getDistance() {
			return distance;
		}

		public void setVelocity(double velocity) {
			this.velocity = velocity;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}
	}

	public double getLeftVelocity() {
		return leftSideData.velocity;
	}

	public double getLeftDistance() {
		return leftSideData.distance;
	}

	public double getRightVelocity() {
		return rightSideData.velocity;
	}

	public double getRightDistance() {
		return rightSideData.distance;
	}

	public double getHeading() {
		return heading;
	}

	public void stop() {
		this.isRunning = false;
	}
	
	public double getVelocity() {
		return (leftSideData.velocity + rightSideData.velocity) / 2;
	}
	
	public double getDistance() {
		return (leftSideData.distance + rightSideData.distance) / 2;
	}
}
