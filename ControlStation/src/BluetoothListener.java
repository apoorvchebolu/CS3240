import java.io.DataInputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;

public class BluetoothListener extends Thread {
	private MainControl mainControl;
	private GUI controlPanel;
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
	private final String malformedMessageError = "h";

	public BluetoothListener(MainControl m, GUI controlPanel) {
		mainControl = m;
		stopRequest = false;
		connection = mainControl.getController().getConnection();
		connectionInput = new DataInputStream(connection.getInputStream());
		this.controlPanel = controlPanel;
	}

	public void run() {
		while (!stopRequest) {
			byte[] buffer = new byte[maxMessageSize];
			int count = 0;
			try {
				count = connectionInput.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (count > 0) {
				String message = (new String(buffer)).trim();
				processMessage(message);
			}
		}
	}

	public void processMessage(String message) { 
		// Method to process messages received from the robot
		System.out.println(message);
		if (message.length() > 8) {
			char opcode = message.charAt(opcodeIndex);
			char messageSourceID = message.charAt(messageSourceIDIndex);
			String receivedChecksum = message.substring(checksumIndex,
					checksumIndex + checksumLength);
			String messageNumber = message.substring(messageNumberIndex,
					messageNumberLength);
			String messageContent = message.substring(messageSourceIDIndex,
					message.length());
			String calculatedChecksum = mainControl.getController()
					.calculateChecksum(messageContent);
			Message receivedMessage = new Message("Received", message);
			if (calculatedChecksum.equals(receivedChecksum)
					&& messageSourceID == 'R') {
				switch (opcode) {
				case 'K': // system status data package
					processSystemStatusData(message);
					receivedMessage
							.setReadableMessageContent("Received system status package");
					mainControl.getController().systemStatusAcknowledgment(
							"" + messageSourceID + messageNumber);
					break;
				case 'L': // execution response
					receivedMessage
							.setReadableMessageContent("Received execution response");
					mainControl.getController()
							.executionResponseAcknowledgment(
									"" + messageSourceID + messageNumber);
					break;
				case 'M': // event error
					receivedMessage
							.setReadableMessageContent("Received event error");
					mainControl.getController().eventErrorAcknowledgment(
							"" + messageSourceID + messageNumber);
					break;
				case 'N': // command acknowledgment
					receivedMessage
							.setReadableMessageContent("Received command acknowledgment");
					break;
				default: // unexpected opcode response
					receivedMessage
							.setReadableMessageContent("Received unknown opcode");
					mainControl.getController().eventError(
							malformedMessageError,
							"" + messageSourceID + messageNumber);
					break;
				}
			}
			mainControl.getMessageHolder().addMessage(receivedMessage);
			controlPanel.updateCommLog(receivedMessage);
		}
	}

	private void processSystemStatusData(String message) { 
		// Helper method to get the robot sensor data
		// from the system status packet and move the data into the correct
		// objects
		int ultrasonicValue = Integer.parseInt(message.substring(
				ultrasonicIndex, ultrasonicIndex + ultrasonicLength));
		int lightValue = Integer.parseInt(message.substring(lightIndex,
				lightIndex + lightLength));
		int soundValue = Integer.parseInt(message.substring(soundIndex,
				soundIndex + soundLength));
		int touchAValue = Integer.parseInt(message.substring(touchAIndex,
				touchAIndex + touchALength));
		int batteryValue = Integer.parseInt(message.substring(batteryIndex,
				batteryIndex + batteryLength));
		int signalStrength = Integer.parseInt(message.substring(signalIndex,
				signalIndex + signalLength));
		int positionX = Integer.parseInt(message.substring(positionXIndex,
				positionXIndex + positionXLength));
		int positionY = Integer.parseInt(message.substring(positionYIndex,
				positionYIndex + positionYLength));
		mainControl.getExtSensor().getUltrasonic().setValue(ultrasonicValue);
		mainControl.getExtSensor().getLight().setValue(lightValue);
		mainControl.getExtSensor().getSound().setValue(soundValue);
		mainControl.getExtSensor().getTouch().setValue(touchAValue);
		mainControl.getIntSensor().getBatteryLife().setValue(batteryValue);
		mainControl.getIntSensor().getSignalStrength().setValue(signalStrength);
		mainControl.getIntSensor().getPositionX().setValue(positionX);
		mainControl.getIntSensor().getPositionY().setValue(positionY);
	}

	public void stopThread() {
		stopRequest = true;
	}
}
