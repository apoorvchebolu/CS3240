
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainControl mainControl = new MainControl();
		BluetoothListener mainListener = new BluetoothListener(mainControl);
		mainListener.start();
		//add lines to create and start GUI here

	}

}
