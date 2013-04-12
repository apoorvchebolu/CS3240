import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;

public class MainControl {
	private InternalSensorDataHolder internalSensors;
	private ExternalSensorDataHolder externalSensors;
	private MessageHolder messageHolder;
	private Controller controller;
	
	public MainControl() {		
		controller = new Controller(this);
		internalSensors = new InternalSensorDataHolder();
		externalSensors = new ExternalSensorDataHolder();
		messageHolder = new MessageHolder();
	}
	public InternalSensorDataHolder getIntSensor() {
		return internalSensors;
	}
	public ExternalSensorDataHolder getExtSensor() {
		return externalSensors;
	}
	public MessageHolder getMessageHolder() {
		return messageHolder;
	}
	public Controller getController() {
		return controller;
	}
}
