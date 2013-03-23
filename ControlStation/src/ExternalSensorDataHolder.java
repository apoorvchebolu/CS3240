
public class ExternalSensorDataHolder {
	private SensorDataItem touch;
	private SensorDataItem light;
	private SensorDataItem sound;
	private SensorDataItem ultrasonic;
	
	public ExternalSensorDataHolder() {
		touch = new SensorDataItem("touch", 0);
		light = new SensorDataItem("light", 0);
		sound = new SensorDataItem("sound", 0);
		ultrasonic = new SensorDataItem("ultrasonic", 0);
	}
	public SensorDataItem getTouch() {
		return touch;
	}
	public SensorDataItem getLight() {
		return light;
	}
	public SensorDataItem getSound() {
		return sound;
	}
	public SensorDataItem getUltrasonic() {
		return ultrasonic;
	}
}
