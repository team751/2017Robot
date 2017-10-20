package org.team751.arduino;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	
	public static class DataInputException extends Exception{
		public DataInputException(String message) {
			super(message);
		}
	}
	private boolean isRunning = true;
	private final int port;
	private double heading; // axes of operation
	// private double distance;
	// private double velocity;

	private ArduinoData leftSideData;
	private ArduinoData rightSideData;

	public ArduinoDataListener(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.01);
		while (true) {
			final String message = port.readString();
			System.out.print(message);
			String[] lines = message.split("\\n");
			System.out.println(Arrays.toString(lines));
			
			final int lastIndex = message.lastIndexOf('\n');
			final int secondLastIndex = message.lastIndexOf('\n', lastIndex - 1);
			String dataLine = message.substring(secondLastIndex, lastIndex);
			String[] data = dataLine.split(",");
			
			if(data[0].equals("left")) {
				leftSideData = new ArduinoData(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
			}else if(data[0].equals("right")) {
				rightSideData = new ArduinoData(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
			}else {
				try {
					throw new DataInputException("Data from Arduino is not in the correct format");
				} catch (DataInputException e) {
						
				}
			}
			heading = Double.parseDouble(data[3]);
			System.out.println("The current data from the left side: " + leftSideData);
			System.out.println("The current data from the right side: " + rightSideData);
			System.out.println("The heading is: " + heading);
			
		}
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
	private class ArduinoData {
		private double velocity;
		private double distance;

		public ArduinoData() {
			this(0, 0);
		}

		public ArduinoData(double velocity, double distance) {
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
		
		@Override
		public String toString() {
			return "[" + velocity + " , " + distance + "]";
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
