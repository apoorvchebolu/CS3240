
public class SensorDataItem {
	private String typeOfDataItem;
	private int value;
	public SensorDataItem(String type, int value) {
		this.typeOfDataItem = type;
		this.value = value;
	}
	public String getTypeOfDataItem() {
		return typeOfDataItem;
	}
	public int getValue() {
		return value;
	}
	public void setTypeOfDataItem(String type) {
		typeOfDataItem = type;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
