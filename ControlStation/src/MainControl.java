import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTInfo;

public class MainControl {
	private InternalSensorDataHolder internalSensors;
	private ExternalSensorDataHolder externalSensors;
	private MessageHolder messageHolder;
	private Controller controller;
	private NXTComm connection;
	
	public MainControl() {		
		controller = new Controller();
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
	public NXTComm getConnection() {
		return connection;
	}
}
