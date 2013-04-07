import java.io.DataInputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;


public class BluetoothListener extends Thread {
	private MainControl mainControl;
	private boolean stopRequest;
	private NXTComm connection;
	private DataInputStream connectionInput;
	private final int maxMessageSize = 256;
	private final int opcodeIndex = 8;
	private final int messageSourceIDIndex = 3;
	private final int checksumIndex = 1;
	private final int checksumLength = 2;
	private final int messageNumberIndex = 4;
	private final int messageNumberLength = 4;
	public BluetoothListener(MainControl m) {
		mainControl = m;
		stopRequest = false;
		connection = mainControl.getController().getConnection();
		connectionInput = new DataInputStream(connection.getInputStream());
	}
	public void run() {
		while(!stopRequest) {
			byte[] buffer = new byte[maxMessageSize];
			int count = 0;
			try {
				count = connectionInput.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (count>0) {
				String message = (new String(buffer)).trim();
				processMessage(message);
			}
		}
	}
	private void processMessage(String message) {
		System.out.println(message);
		if(message.length() > 8) {
			char opcode = message.charAt(opcodeIndex);
			char messageSourceID = message.charAt(messageSourceIDIndex);
			String receivedChecksum = message.substring(checksumIndex, checksumIndex + checksumLength);
			String messageNumber = message.substring(messageNumberIndex, messageNumberLength);
			String messageContent = message.substring(messageSourceIDIndex, message.length());
			String calculatedChecksum = mainControl.getController().calculateChecksum(messageContent);
			if(calculatedChecksum.equals(receivedChecksum) && messageSourceID == 'R') {
				switch(opcode) {
				case 'K': //system status data package
					processSystemStatusData(message);
					mainControl.getController().systemStatusAcknowledgment("" + messageSourceID + messageNumber);
					break;
				case 'L': //execution response
					mainControl.getController().executionResponseAcknowledgment("" + messageSourceID + messageNumber);
					break;
				case 'M': //event error
					mainControl.getController().eventErrorAcknowledgment("" + messageSourceID + messageNumber);
					break;
				case 'N': //command acknowledgment
					//not implemented yet
					break;
				default: //unexpected opcode response
					//not implemented yet
					break;
				}
			}
		}
	}
	private void processSystemStatusData(String message) {
		int ultrasonicValue = Integer.parseInt(message.substring(9, 13));
		int lightValue = Integer.parseInt(message.substring(13, 17));
		int soundValue = Integer.parseInt(message.substring(17, 21));
		int touchAValue = Integer.parseInt(message.substring(21,22));
		//	int touchBValue = (int)(message.charAt(22));
		int batteryValue = Integer.parseInt(message.substring(23, 27));
		int signalStrength = Integer.parseInt(message.substring(27, 31));
		int positionX = Integer.parseInt(message.substring(31, 35));
		int positionY = Integer.parseInt(message.substring(35, 39));
		mainControl.getExtSensor().getUltrasonic().setValue(ultrasonicValue);
		mainControl.getExtSensor().getLight().setValue(lightValue);
		mainControl.getExtSensor().getSound().setValue(soundValue);
		mainControl.getExtSensor().getTouch().setValue(touchAValue);
		mainControl.getIntSensor().getBatteryLife().setValue(batteryValue);
		mainControl.getIntSensor().getSignalStrength().setValue(signalStrength);
		mainControl.getIntSensor().getPositionX().setValue(positionX);
		mainControl.getIntSensor().getPositionY().setValue(positionY);
	}
	/*private int convert4BytesToInteger(String bytes) {
		return (int)(bytes.charAt(3)) + (int)(bytes.charAt(2)) * 256 + (int)(bytes.charAt(1)) * 65536 + (int)(bytes.charAt(0)) * 16777216;
	}*/
}
