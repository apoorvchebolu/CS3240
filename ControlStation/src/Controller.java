import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Controller {
	private NXTComm connection;
	private NXTInfo[] info;
	private DataOutputStream connectionOutputStream;
	private String headerString = "#";
	private String messageSourceID = "S";
	private String endString = "" + (char)0;
	private int messageNumber;
	private boolean connected;
	private int robotSpeed;
	private int currentRobotAngle;
	public Controller(NXTComm conn, NXTInfo[] info) {
		connection = conn;
		this.info = info;
		messageNumber = 0;
		connected = false;
		robotSpeed = 0;
		currentRobotAngle = 0;
	}
	public void connect() {
		if (info.length == 0) {
			System.out.println("Device not found");
		}
		else {
			try {
				connection.open(info[0]);
				connectionOutputStream = new DataOutputStream(connection.getOutputStream());
				connected = true;
			}
			catch (NXTCommException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void goHome() {
		//to be implemented later
	}
	public void requestSystemStatusData() {
		if(connected) {
			String opcode = "S";
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void turn90Left() {
		//to be implemented later
	}
	public void turn90Right() {
		//to be implemented later
	}
	public void turn180() {
		//to be implemented later
	}
	public void autoAvoid() {
		//to be implemented later
	}
	public void setSpeed(int speed) {
		robotSpeed = speed;
	}
	public int getSpeed() {
		return robotSpeed;
	}
	public void stop() {
		if(connected) {
			String opcode = "F";
			String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void moveForward() {
		if(connected) {
			String opcode = "C";
			String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint + format4ByteNumber(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void moveBackward() {
		if(connected) {
			String opcode = "D";
			String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint + format4ByteNumber(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void moveLeft() {
		if(connected) {
			String opcode = "A";
			String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint + format4ByteNumber(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void moveRight() {
		if(connected) {
			String opcode = "B";
			String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint + format4ByteNumber(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void executionResponseAcknowledgment(String messageIDParameter) {
		if(connected) {
			String opcode = "O";
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + messageIDParameter;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void systemStatusAcknowledgment(String messageIDParameter) {
		if(connected) {
			String opcode = "P";
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + messageIDParameter;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	public void eventErrorAcknowledgment(String messageIDParameter) {
		if(connected) {
			String opcode = "Q";
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + messageIDParameter;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			try {
				connectionOutputStream.write(message.getBytes());
				connectionOutputStream.flush();
				messageNumber++;
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}

	private String format4ByteNumber(int number) {
		return "" + (char)((number/16777216)%256) + (char)((number/65536)%256) +
				(char)((number/256)%256) + (char)((number)%256);
	}
	public String calculateChecksum(String messageContent) {
		int checksum = 0;
		for(int i = 0; i < messageContent.length(); i++) {
			checksum += (int)messageContent.charAt(i);
		}
		return formatChecksum(checksum);
	}
	private static String formatChecksum(int checksum) {
		return "" + (char)((checksum/256)%256) + (char)(checksum %256);
	}
}
