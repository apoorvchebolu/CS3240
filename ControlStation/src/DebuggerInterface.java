import java.awt.EventQueue;


import javax.swing.*;
/*import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;*/
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Scanner;
import javax.swing.Timer;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class DebuggerInterface {

	private JFrame frame;
	//private JTextField systemVarBox = new JTextField();
	private JTextArea systemVarBox = new JTextArea();
	static CommunicationLog communicationLog = new CommunicationLog();
	DebuggerCommunicator debugCom = new DebuggerCommunicator();
	boolean [] breakpoints = new boolean [5]; //The ArrayList of breakpoints
	private static boolean USBtest = false;
	static long start = 0, latency = 0;
	static Boolean readFlag = true;
	static Object lock = new Object();
	static DataOutputStream os;
	static DataInputStream is;
	
	JLabel lblCommunicationLog;
	JLabel lblSensorHistory;
	final static JTextArea sensorLog = new JTextArea();
	final static JTextArea batteryStrength = new JTextArea();
	final static JTextArea signalStrength = new JTextArea();
	final static JTextArea soundData = new JTextArea();
	final static JTextArea lightData = new JTextArea();
	final static JTextArea touchData = new JTextArea();
	final static JTextArea ultraSonic = new JTextArea();
	final static JTextArea ASpeed = new JTextArea();
	final static JTextArea AAngle = new JTextArea();
	final static JTextArea BSpeed = new JTextArea();
	final static JTextArea BAngle = new JTextArea();
	final JTextArea commLog = new JTextArea();
	
	
	
	static ArrayList<String> touchHistory = new ArrayList<String>();
	static ArrayList<String> soundHistory = new ArrayList<String>();
	static ArrayList<String> ultrasonicHistory = new ArrayList<String>();
	static ArrayList<String> lightHistory = new ArrayList<String>();
	/**
	 * Launch the application.
	 * @throws NXTCommException 
	 */
	public void startDebugger(NXTComm connection) {
	//public static void main(String[] args) throws NXTCommException {
		//Code to establish the bluetooth connection
		
		/*final NXTComm connection = connectionInput
		final NXTComm connection;
		NXTInfo[] info;

		if (USBtest) {
			connection = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			info = connection.search(null, 0); 
												
		} else {
			connection = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			info = connection.search("LEAD4", 1111); 
		}

		if (info.length == 0) {
			System.out.println("Cant find any device to connect");
			return;
		}
		connection.open(info[0]);*/
		
		os = new DataOutputStream(connection.getOutputStream());
		is = new DataInputStream(connection.getInputStream());

		
		//Code to make the GUI window
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebuggerInterface window = new DebuggerInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		Scanner scanner = new Scanner(System.in);*/

		// Start a reader thread and readFlag is our barrier (i.e. set readFlag to false to terminate threads)
		Thread PCreceiver = new Thread() {
			public void run() {
				while (readFlag) {
					try {
						start = System.currentTimeMillis();
						byte[] buffer = new byte[256];
						int count = is.read(buffer); 
						if (count>0){
						String returnedInfo = (new String(buffer)).trim();
						System.out.println(returnedInfo);
						applyInformation(returnedInfo);
						
						long l = System.currentTimeMillis() - start;
						System.out.printf("NXJ: %s [%dms]\n", returnedInfo, l);
						}
						Thread.sleep(10);
					} catch (IOException e) {
						System.out.println("Fail to read from 'is' bc "
								+ e.toString());
						return;
					} catch (InterruptedException e){
						
					}

				}
			}
		};
		PCreceiver.run();
		Timer myTimer= new Timer (1000, new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String encodedMessage = debugCom.requestSystemStatusData();
		    	String encodedMessage2 = debugCom.requestMotorData();
				try {
					os.write(encodedMessage.getBytes());
					os.flush();
					os.write(encodedMessage2.getBytes());
					os.flush();
				} catch (IOException evt) {
					// TODO Auto-generated catch block
					evt.printStackTrace();
				}
		    }
		});
		
		myTimer.start();
		
	}

	/**
	 * Create the application.
	 */
	public DebuggerInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GREEN);
		frame.setBounds(100, 100, 858, 743);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("Emergency Stop");
		btnNewButton.setBounds(34, 24, 136, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				emergencyStop();
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnReboot = new JButton("Reboot");
		btnReboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reboot();
			}
		});
		btnReboot.setBounds(34, 58, 136, 23);
		frame.getContentPane().add(btnReboot);
		
		JLabel lblNewLabel = new JLabel("Battery Strength");
		lblNewLabel.setBounds(23, 100, 96, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Signal Strength");
		lblNewLabel_1.setBounds(23, 126, 96, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblSound = new JLabel("Sound");
		lblSound.setBounds(23, 151, 46, 14);
		frame.getContentPane().add(lblSound);
		
		JLabel lblLight = new JLabel("Light");
		lblLight.setBounds(23, 176, 46, 14);
		frame.getContentPane().add(lblLight);
		
		JLabel lblTouch = new JLabel("Touch");
		lblTouch.setBounds(23, 199, 46, 14);
		frame.getContentPane().add(lblTouch);
		
		JLabel lblUltrasonic = new JLabel("Ultrasonic");
		lblUltrasonic.setBounds(23, 222, 67, 14);
		frame.getContentPane().add(lblUltrasonic);
		
		JLabel lblMotorASpeed = new JLabel("Motor A Speed");
		lblMotorASpeed.setBounds(23, 247, 79, 14);
		frame.getContentPane().add(lblMotorASpeed);
		
		JLabel lblMotorBSpeed = new JLabel("Motor A Angle");
		lblMotorBSpeed.setBounds(23, 275, 102, 14);
		frame.getContentPane().add(lblMotorBSpeed);
		
		JLabel lblMotorBSpeed_1 = new JLabel("Motor B Speed");
		lblMotorBSpeed_1.setBounds(23, 302, 79, 14);
		frame.getContentPane().add(lblMotorBSpeed_1);
		
		JLabel lblMotorBAngle = new JLabel("Motor B Angle");
		lblMotorBAngle.setBounds(23, 325, 79, 14);
		frame.getContentPane().add(lblMotorBAngle);
		
		final JList list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String encodedMessage = debugCom.executeMessage((String)list.getSelectedValue());
				try {
					os.write(encodedMessage.getBytes());
					os.flush();
					String opCode = encodedMessage.substring(8,9);
					Entry newEntry = new Entry("Message sent to robot: " + encodedMessage, true, opCode);
					communicationLog.addEntry(newEntry);
					displayDebugVariables();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(list.getSelectedValue());
			}
		});
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"On move left", "On move right", "On move forward", "On move backward", "On stop"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(23, 380, 137, 111);
		frame.getContentPane().add(list);
		
		//batteryStrength = new JTextArea();
		batteryStrength.setBounds(129, 103, 52, 14);
		frame.getContentPane().add(batteryStrength);
		
		//signalStrength = new JTextArea();
		signalStrength.setBounds(129, 126, 52, 14);
		frame.getContentPane().add(signalStrength);
		
		//soundData = new JTextArea();
		soundData.setBounds(129, 151, 52, 14);
		frame.getContentPane().add(soundData);
		
		//lightData = new JTextArea();
		lightData.setBounds(129, 171, 52, 14);
		frame.getContentPane().add(lightData);
		
		//touchData = new JTextArea();
		touchData.setBounds(129, 202, 52, 14);
		frame.getContentPane().add(touchData);
		
		//ultraSonic = new JTextArea();
		ultraSonic.setBounds(129, 225, 52, 14);
		frame.getContentPane().add(ultraSonic);
		
		//ASpeed = new JTextArea();
		ASpeed.setBounds(129, 250, 52, 14);
		frame.getContentPane().add(ASpeed);
		
		//AAngle = new JTextArea();
		AAngle.setBounds(129, 278, 52, 14);
		frame.getContentPane().add(AAngle);
		
		//BSpeed = new JTextArea();
		BSpeed.setBounds(129, 305, 52, 14);
		frame.getContentPane().add(BSpeed);
		
		//BAngle = new JTextArea();
		BAngle.setBounds(129, 328, 52, 14);
		frame.getContentPane().add(BAngle);
		
		//final JTextArea commLog = new JTextArea();
		commLog.setBounds(223, 74, 253, 448);
		frame.getContentPane().add(commLog);
		JScrollPane commPane = new JScrollPane(commLog);
		commPane.setBounds(223,74,253,448);
		frame.getContentPane().add(commPane);
		
		final JRadioButton rdbtnNewRadioButton = new JRadioButton("All");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnNewRadioButton.isSelected()){
					System.out.println("selected");
					displayCommunicationLog(1);
					
				}else{
					System.out.println("deselected");
					commLog.setText("");
					
				}
			}
		});
		rdbtnNewRadioButton.setBounds(223, 44, 46, 23);
		frame.getContentPane().add(rdbtnNewRadioButton);
		
		final JRadioButton rdbtnSent = new JRadioButton("Sent");
		rdbtnSent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnSent.isSelected()){
					System.out.println("selected");
					displayCommunicationLog(2);
					
				}else{
					System.out.println("deselected");
					commLog.setText("");
					
				}
			}
		});
		rdbtnSent.setBounds(271, 44, 58, 23);
		frame.getContentPane().add(rdbtnSent);
		
		final JRadioButton rdbtnReceived = new JRadioButton("Received");
		rdbtnReceived.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnReceived.isSelected()){
					System.out.println("selected");
					displayCommunicationLog(3);
					
				}else{
					System.out.println("deselected");
					commLog.setText("");
					
				}
			}
		});
		rdbtnReceived.setBounds(331, 44, 79, 23);
		frame.getContentPane().add(rdbtnReceived);
		
		sensorLog.setBounds(506, 74, 280, 448);
		frame.getContentPane().add(sensorLog);
		JScrollPane sensorPane = new JScrollPane(sensorLog);
		sensorPane.setBounds(506,74,280,448);
		frame.getContentPane().add(sensorPane);
		
		JLabel lblCommunicationLog = new JLabel("Communication Log");
		lblCommunicationLog.setBounds(223, 28, 162, 14);
		frame.getContentPane().add(lblCommunicationLog);
		
		final JRadioButton rdbtnSound = new JRadioButton("Sound");
		rdbtnSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnSound.isSelected()){
					System.out.println("selected");
					displaySensorDataHistory(1);
					
				}else{
					System.out.println("deselected");
					sensorLog.setText("");
					
				}
			}
		});
		rdbtnSound.setBounds(506, 44, 67, 23);
		frame.getContentPane().add(rdbtnSound);
		
		final JRadioButton rdbtnTouch = new JRadioButton("Touch");
		rdbtnTouch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnTouch.isSelected()){
					System.out.println("selected");
					displaySensorDataHistory(2);
					
				}else{
					System.out.println("deselected");
					sensorLog.setText("");
					
				}
			}
		});
		rdbtnTouch.setBounds(578, 44, 67, 23);
		frame.getContentPane().add(rdbtnTouch);
		
		final JRadioButton rdbtnLight = new JRadioButton("Light");
		rdbtnLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnLight.isSelected()){
					System.out.println("selected");
					displaySensorDataHistory(3);
					
				}else{
					System.out.println("deselected");
					sensorLog.setText("");
					
				}
			}
		});
		rdbtnLight.setBounds(647, 44, 58, 23);
		frame.getContentPane().add(rdbtnLight);
		
		final JRadioButton rdbtnUltrasonic = new JRadioButton("Ultrasonic");
		rdbtnUltrasonic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnUltrasonic.isSelected()){
					System.out.println("selected");
					displaySensorDataHistory(4);
					
				}else{
					System.out.println("deselected");
					sensorLog.setText("");
					
				}
			}
		});
		rdbtnUltrasonic.setBounds(707, 44, 79, 23);
		frame.getContentPane().add(rdbtnUltrasonic);
		
		//systemVarBox = new JTextField();
		//systemVarBox = new JTextArea();
		systemVarBox.setBounds(74, 560, 402, 134);
		frame.getContentPane().add(systemVarBox);
		//systemVarBox.setColumns(10);
		JScrollPane sysVarPane = new JScrollPane(systemVarBox);
		sysVarPane.setBounds(74,560,402,134);
		frame.getContentPane().add(sysVarPane);
		
		JLabel lblSystemVariables = new JLabel("System Variables");
		lblSystemVariables.setBounds(74, 535, 107, 14);
		frame.getContentPane().add(lblSystemVariables);
		
		lblSensorHistory = new JLabel("Sensor History");
		lblSensorHistory.setBounds(506, 28, 96, 14);
		frame.getContentPane().add(lblSensorHistory);
		
		JButton btnGetVariableValues = new JButton("Get Variable Values");
		btnGetVariableValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String encodedMessage = debugCom.requestSystemStatusData();
				try {
					os.write(encodedMessage.getBytes());
					os.flush();
					String opCode = encodedMessage.substring(8,9);
					Entry newEntry = new Entry("Message sent to robot: " + encodedMessage, true, opCode);
					communicationLog.addEntry(newEntry);
					displayDebugVariables();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnGetVariableValues.setBounds(19, 350, 162, 23);
		frame.getContentPane().add(btnGetVariableValues);
		frame.setVisible(true);
	}
	
	//Need to implement
	public void displayDebugVariables(){
		String [] internalVariables = debugCom.getInternalVariables();
		String variables = "";
		for (int i = 0; i < internalVariables.length; i++){
			variables = variables + internalVariables[i] + '\n';
		}
		
		variables = variables + "Output Stream : " + os.toString() + '\n';
		variables = variables + "Input Stream : " + is.toString() + '\n';
		systemVarBox.setText(variables);
		
	}
	
	public void displaySensorDataHistory(int option) {
		String messages = "";
		if (option == 1){
			//Sound
			for (int i = 0; i < soundHistory.size(); i++){
				messages = messages + soundHistory.get(i) + '\n';
			}
		} else if (option == 2){
			//Touch
			for (int i = 0; i < touchHistory.size(); i++){
				messages = messages + touchHistory.get(i) + '\n';
			}
		} else if (option == 3){
			//Light
			for (int i = 0; i < lightHistory.size(); i++){
				messages = messages + lightHistory.get(i) + '\n';
			}
		} else if (option == 4){
			//Ultrasonic
			for (int i = 0; i < ultrasonicHistory.size(); i++){
				messages = messages + ultrasonicHistory.get(i) + '\n';
			}
		}
		sensorLog.setText(messages);
		
	}
	//Displays each historical timestamped reading of the sensory data
	
	public void displayCommunicationLog(int option){
		ArrayList<Entry> log = communicationLog.getLog();
		
		String messages = "";
		
		if (option == 1){
			for (int i = 0; i < log.size(); i++){
				messages = messages + log.get(i).getEntry() + "\n";
				commLog.setText(messages);
			}
		} else if (option == 2){
			for (int i = 0; i < log.size(); i++){
				if (log.get(i).getSentStatus() == true){
					messages = messages + log.get(i).getEntry() + '\n';
					commLog.setText(messages);
				}
				
			}
			
		} else if (option == 3){
			for (int i = 0; i < log.size(); i++){
				if (log.get(i).getSentStatus() == false){
					messages = messages + log.get(i).getEntry() + '\n';
					commLog.setText(messages);
				}
				
			}
		}
	}
	//Returns the entirety of the communication log,
	//which contains all the messages sent between the robot
	//base station
		
	public void reboot(){
		String encodedMessage = debugCom.reboot();
		try {
			os.write(encodedMessage.getBytes());
			os.flush();
			String opCode = encodedMessage.substring(8,9);
			Entry newEntry = new Entry("Message sent to robot: " + encodedMessage, true, opCode);
			communicationLog.addEntry(newEntry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		soundHistory.clear();
		touchHistory.clear();
		ultrasonicHistory.clear();
		lightHistory.clear();
		
		touchData.setText("");
		lightData.setText("");
		ultraSonic.setText("");
		soundData.setText("");
		batteryStrength.setText("");
		signalStrength.setText("");
		AAngle.setText("");
		ASpeed.setText("");
		BAngle.setText("");
		BSpeed.setText("");
		
		communicationLog.clearAll();
	}
	//Reboots the robot
	
	public void emergencyStop(){
		String encodedMessage = debugCom.stop();
		try {
			os.write(encodedMessage.getBytes());
			os.flush();
			String opCode = encodedMessage.substring(8,9);
			Entry newEntry = new Entry("Message sent to robot: " + encodedMessage, true, opCode);
			communicationLog.addEntry(newEntry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Every message that is sent from the robot gets recorded. Also, if the info sent back is sensor data, it applies it appropriately
	public static void applyInformation(String input){
		String opCode = input.substring(8,9);
		//System.out.println(opCode);
		Entry newEntry = new Entry("Message recieved from robot: " + input, false, opCode);
		communicationLog.addEntry(newEntry);
		
		if (opCode.equals("K")){
			String touchDataI = input.substring(21,25);
			touchHistory.add(touchDataI);
			touchData.setText(touchDataI);
			String lightDataI = input.substring(13,17);
			lightHistory.add(lightDataI);
			lightData.setText(lightDataI);
			String ultrasonicDataI = input.substring(9,13);
			ultrasonicHistory.add(ultrasonicDataI);
			ultraSonic.setText(ultrasonicDataI);
			String soundDataI = input.substring(17,21);
			soundHistory.add(soundDataI);
			soundData.setText(soundDataI);
			String batteryDataI = input.substring(25,29);
			batteryStrength.setText(batteryDataI);
			String signalDataI = input.substring(29,33);
			signalStrength.setText(signalDataI);
		}
		
		if (opCode.equals("Z")){
			System.out.println("Motor Info " + input);
			String motorAAngle = input.substring(9,13);
			AAngle.setText(motorAAngle);
			String motorASpeed = input.substring(13,17);
			ASpeed.setText(motorASpeed);
			String motorBAngle = input.substring(17,21);
			BAngle.setText(motorBAngle);
			String motorBSpeed = input.substring(21,25);
			BSpeed.setText(motorBSpeed);
			
			
		}
		
	}
	public JFrame getFrame() {
		return frame;
	}
}

