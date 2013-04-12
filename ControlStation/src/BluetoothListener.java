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
	private final int ultrasonicIndex = 9;
	private final int ultrasonicLength = 4;
	private final int lightIndex = 13;
	private final int lightLength = 4;
	private final int soundIndex = 17;
	private final int soundLength = 4;
	private final int touchAIndex = 21;
	private final int touchALength = 1;
	private final int batteryIndex = 23;
	private final int batteryLength = 4;
	private final int signalIndex = 27;
	private final int signalLength = 4;
	private final int positionXIndex = 31;
	private final int positionXLength = 4;
	private final int positionYIndex = 35;
	private final int positionYLength = 4;
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
	public void processMessage(String message) { //Method to process messages received from the robot
		System.out.println(message);
		if(message.length() > 8) {
			char opcode = message.charAt(opcodeIndex);
			char messageSourceID = message.charAt(messageSourceIDIndex);
			String receivedChecksum = message.substring(checksumIndex, checksumIndex + checksumLength);
			String messageNumber = message.substring(messageNumberIndex, messageNumberLength);
			String messageContent = message.substring(messageSourceIDIndex, message.length());
			String calculatedChecksum = mainControl.getController().calculateChecksum(messageContent);
			System.out.println(calculatedChecksum + "  " + receivedChecksum);
			if(calculatedChecksum.equals(receivedChecksum) && messageSourceID == 'R') {
				mainControl.getMessageHolder().addMessage(new Message("Received", message));
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
		int ultrasonicValue = Integer.parseInt(message.substring(ultrasonicIndex, ultrasonicIndex + ultrasonicLength));
		int lightValue = Integer.parseInt(message.substring(lightIndex, lightIndex + lightLength));
		int soundValue = Integer.parseInt(message.substring(soundIndex, soundIndex + soundLength));
		int touchAValue = Integer.parseInt(message.substring(touchAIndex, touchAIndex + touchALength));
		//	int touchBValue = (int)(message.charAt(22));
		int batteryValue = Integer.parseInt(message.substring(batteryIndex, batteryIndex + batteryLength));
		int signalStrength = Integer.parseInt(message.substring(signalIndex, signalIndex + signalLength));
		int positionX = Integer.parseInt(message.substring(positionXIndex, positionXIndex + positionXLength));
		int positionY = Integer.parseInt(message.substring(positionYIndex, positionYIndex + positionYLength));
		mainControl.getExtSensor().getUltrasonic().setValue(ultrasonicValue);
		mainControl.getExtSensor().getLight().setValue(lightValue);
		mainControl.getExtSensor().getSound().setValue(soundValue);
		mainControl.getExtSensor().getTouch().setValue(touchAValue);
		mainControl.getIntSensor().getBatteryLife().setValue(batteryValue);
		mainControl.getIntSensor().getSignalStrength().setValue(signalStrength);
		mainControl.getIntSensor().getPositionX().setValue(positionX);
		mainControl.getIntSensor().getPositionY().setValue(positionY);
		//System.out.println("system status processed");
	}
}
