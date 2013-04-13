import java.io.DataOutputStream;
import java.io.IOException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Controller {
	private NXTComm connection;
	private NXTInfo[] info;
	private MainControl mainControl;
	private DataOutputStream connectionOutputStream;
	private String headerString = "#";
	private String messageSourceID = "S";
	private String endString = "" + (char)0;
	private int messageNumber;
	private boolean connected;
	private int robotSpeed;
	private int currentRobotAngle;	
	final private int maxNumberValue = 10000;
	final private String zeroString = "0000";
	
	public Controller(MainControl mainControl) {
		messageNumber = 0;
		connected = false;
		robotSpeed = 6;
		currentRobotAngle = 0;
		connect();
		this.mainControl = mainControl;
	}
	
	//initiates the connection to the robot
	public void connect() {
		try {
			connection = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			info = connection.search("LEAD4", 1111);
		}
		catch (NXTCommException e) {
			System.out.println(e.toString());
		}
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
	
	//sends a packet requesting the system status packet from the robot
	public void requestSystemStatusData() {
		if(connected) {
			String opcode = "S";
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
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
	
	//sends a packet telling the robot to stop
	public void stop() {
		if(connected) {
			String opcode = "F";
			String breakpoint = zeroString;
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode + breakpoint;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}
	
	//sends a packet telling the robot to move forwards
	public void moveForward(boolean curveLeft, boolean curveRight) {
		int leftMotorSpeed = robotSpeed;
		int rightMotorSpeed = robotSpeed;
		if(curveLeft) {
			leftMotorSpeed = robotSpeed / 2;
		}
		if(curveRight) {
			rightMotorSpeed = robotSpeed / 2;
		}
		if(connected) {
			String opcode = "C";
			String breakpoint = zeroString;
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode
					+ breakpoint + intTo4CharacterString(leftMotorSpeed) + intTo4CharacterString(rightMotorSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}	
	
	//sends a packet telling the robot to move backwards
	public void moveBackward(boolean curveLeft, boolean curveRight) {
		int leftMotorSpeed = robotSpeed;
		int rightMotorSpeed = robotSpeed;
		if(curveLeft) {
			leftMotorSpeed = robotSpeed / 2;
		}
		if(curveRight) {
			rightMotorSpeed = robotSpeed / 2;
		}
		if(connected) {
			String opcode = "D";
			String breakpoint = zeroString;
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode
					+ breakpoint + intTo4CharacterString(leftMotorSpeed) + intTo4CharacterString(rightMotorSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}
	
	//sends a packet telling the robot to turn left
	public void moveLeft() {
		if(connected) {
			String opcode = "A";
			String breakpoint = zeroString;
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode + breakpoint + intTo4CharacterString(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}
	
	//sends a packet telling the robot to turn right
	public void moveRight() {
		if(connected) {
			String opcode = "B";
			String breakpoint = zeroString;
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode + breakpoint + intTo4CharacterString(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}
	
	//sends an execution response acknowledgment packet
	public void executionResponseAcknowledgment(String messageIDParameter) {
		if(connected) {
			String opcode = "O";
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode + messageIDParameter;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}
	
	//sends a system status acknowledgment packet
	public void systemStatusAcknowledgment(String messageIDParameter) {
		if(connected) {
			String opcode = "P";
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode + messageIDParameter;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}
	
	//sends an event error acknowledgment packet
	public void eventErrorAcknowledgment(String messageIDParameter) {
		if(connected) {
			String opcode = "Q";
			String message = messageSourceID + intTo4CharacterString(messageNumber) + opcode + messageIDParameter;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			sendMessage(message);
		}
	}

	private void sendMessage(String message) {
		try {
			connectionOutputStream.write(message.getBytes());
			connectionOutputStream.flush();
			messageNumber++;
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		mainControl.getMessageHolder().addMessage(new Message("Sent", message));
		
	}
	public String calculateChecksum(String messageContent) {
		int checksum = 0;
		for(int i = 0; i < messageContent.length(); i++) {
			checksum += (int)messageContent.charAt(i);
		}
		return formatChecksum(checksum);
	}
	private String formatChecksum(int checksum) {
		String checksumString = "" + checksum % 100;
		checksumString = zeroString.substring(0, 2 - checksumString.length()) + checksumString;
		return checksumString;
	}
	private String intTo4CharacterString(int numberToConvert) {
		String numberAsString = "" + numberToConvert%maxNumberValue;
		numberAsString = zeroString.substring(0, 4 - numberAsString.length()) + numberAsString;
		return numberAsString;
	}
	public NXTComm getConnection() {
		return connection;
	}
}
