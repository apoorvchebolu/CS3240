
public class InternalSensorDataHolder {
	private SensorDataItem temperature;
	private SensorDataItem batteryLife;
	private SensorDataItem speed;
	private SensorDataItem positionX;
	private SensorDataItem positionY;
	private SensorDataItem signalStrength;
	public InternalSensorDataHolder() {
		temperature = new SensorDataItem("temperature", 0);
		batteryLife = new SensorDataItem("batteryLife", 0);
		speed = new SensorDataItem("speed", 0);
		positionX = new SensorDataItem("positionX", 0);
		positionY = new SensorDataItem("positionY", 0);
		signalStrength = new SensorDataItem("signalStrength", 0);
	}
	public SensorDataItem getTemperature() {
		return temperature;
	}
	public SensorDataItem getBatteryLife() {
		return batteryLife;
	}
	public SensorDataItem getSpeed() {
		return speed;
	}
	public SensorDataItem getPositionX() {
		return positionX;
	}
	public SensorDataItem getPositionY() {
		return positionY;
	}
	public SensorDataItem getSignalStrength() {
		return signalStrength;
	}
}
