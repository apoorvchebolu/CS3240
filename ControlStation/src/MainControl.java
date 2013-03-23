import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class MainControl {
	private InternalSensorDataHolder internalSensors;
	private ExternalSensorDataHolder externalSensors;
	private MessageHolder messageHolder;
	private Controller controller;
	private NXTComm connection;
	private NXTInfo[] info;
	
	public MainControl() {
		try {
			connection = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			info = connection.search("NXT", 1234);
		}
		catch (NXTCommException e) {
			System.out.println(e.toString());
		}
		controller = new Controller(connection, info);
		controller.connect();
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
