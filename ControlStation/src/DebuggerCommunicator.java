import java.io.IOException;
import java.util.ArrayList;


public class DebuggerCommunicator {
	private String headerString = "#";
	private String messageSourceID = "D";
	private int messageNumber;
	private boolean connected;
	private int robotSpeed;
	private int currentRobotAngle;
	private String endString = "" + (char)0;
	
	private String lastOpCode = "";
	private String lastBreakPoint = "";
	private String lastMessage = "";
	private String lastCheckSum = "";
	private String [] varArray = new String [5];
	
	/*
	public ArrayList<Message> getMessageList() {
		return null;
	}
	*/
	//Returns the entirety of the message list, which is 
	//the collection of all the possible messages that the robot 
	//can parse
	
	//Need to implement
	public String evaluateError(char c) {
		return "";
	}
	//Takes in an error code, interprets it
	//and returns the full error message.
	
	
	
	//This is called when the debugger sends a breakpoint around a command
	public String executeMessage(String message){
		String encodedMessage = "";
		switch (message) {
			case "On move left": 
				encodedMessage = moveLeft();
				return encodedMessage;
		case "On move right":
				encodedMessage = moveRight();
				return encodedMessage;
			case "On move forward":
				encodedMessage = moveForward();
				return encodedMessage;
			case "On move backward":
				encodedMessage = moveBackward();
				return encodedMessage;
			case "On stop":
				encodedMessage = stop();
				return encodedMessage;

		}
		return encodedMessage;
	}
	
	
	public String stop() {
			String opcode = "F";
			String breakpoint = "" + (char)1 + (char)1 + (char)1 + (char)1;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			
			varArray[0] = opcode;
			varArray[1] = breakpoint;
			varArray[2] = message;
			varArray[3] = checksum;
			return message;
	}
	public String moveForward() {
			String opcode = "C";
			String breakpoint = "" + (char)1 + (char)1 + (char)1 + (char)1;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint + format4ByteNumber(robotSpeed) + format4ByteNumber(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			
			varArray[0] = opcode;
			varArray[1] = breakpoint;
			varArray[2] = message;
			varArray[3] = checksum;
			return message;
		
	}
	public String moveBackward() {
			String opcode = "D";
			String breakpoint = "" + (char)1 + (char)1 + (char)1 + (char)1;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint + format4ByteNumber(robotSpeed)+ format4ByteNumber(robotSpeed);
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			
			varArray[0] = opcode;
			varArray[1] = breakpoint;
			varArray[2] = message;
			varArray[3] = checksum;
			return message;
	}
	public String moveLeft() {
			String opcode = "A";
			String breakpoint = "" + (char)1 + (char)1 + (char)1 + (char)1;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			
			varArray[0] = opcode;
			varArray[1] = breakpoint;
			varArray[2] = message;
			varArray[3] = checksum;
			return message;
	}
	public String moveRight() {
			String opcode = "B";
			String breakpoint = "" + (char)1 + (char)1 + (char)1 + (char)1;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode + breakpoint;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			
			varArray[0] = opcode;
			varArray[1] = breakpoint;
			varArray[2] = message;
			varArray[3] = checksum;
			return message;
	}
	
	public String requestSystemStatusData() {
		
			String opcode = "S";
			String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
			String message = messageSourceID + format4ByteNumber(messageNumber) + opcode;
			String checksum = calculateChecksum(message);
			message = headerString + checksum + message + endString;
			
			varArray[0] = opcode;
			varArray[1] = breakpoint;
			varArray[2] = message;
			varArray[3] = checksum;
			return message;
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

	public String requestMotorData() {
		String opcode = "Z";
		String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
		String message = messageSourceID + format4ByteNumber(messageNumber) + opcode;
		String checksum = calculateChecksum(message);
		message = headerString + checksum + message + endString;
		
		varArray[0] = opcode;
		varArray[1] = breakpoint;
		varArray[2] = message;
		varArray[3] = checksum;
		return message;
	}
	
	public String reboot(){
		String opcode = "Y";
		String breakpoint = "" + (char)0 + (char)0 + (char)0 + (char)0;
		String message = messageSourceID + format4ByteNumber(messageNumber) + opcode;
		String checksum = calculateChecksum(message);
		message = headerString + checksum + message + endString;
		
		return message;
	}
	
	public String[] getInternalVariables(){
		
		return varArray;
		
	}
}
