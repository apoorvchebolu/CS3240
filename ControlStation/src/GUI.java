import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class GUI
{
	static int padDisplaceX = 200;
	static int padDisplaceY = 10;
	static String newline = "\n";
	
	private ArrayList<String> keysCurrentlyPressed;

	JPanel data1, data2, data3, buttons1, buttons2;
	JLabel touch1label, touch2label, lightlabel, soundlabel, ultralabel;
	JTextField touch1data, touch2data, lightdata, sounddata, ultradata;
	JButton connect, home, requestdata, stop, forward, backward, left, right;
	JTextArea commlog;
	MainControl control;

	public GUI(MainControl m)
	{
		control = m;
		keysCurrentlyPressed = new ArrayList<String>();
	}

	public JPanel createContentPane (){  //Initialize the objects in the GUI, including buttons, labels, and 
											//action listeners
		
		//panels
		final JPanel basepane = new JPanel();
		basepane.setLayout(null);

		//To be used to display internal sensor data (battery level, signal strength)
		data1 = new JPanel();
		data1.setLayout(null);
		data1.setLocation(30,30);
		data1.setSize(200, 300);
		basepane.add(data1);

		//Used to contain display objects for external sensor data values (touch, sound, light, ultrasonic)
		JPanel data2 = new JPanel();
		data2.setLayout(null);
		data2.setLocation(280,30);
		data2.setSize(200, 300);
		basepane.add(data2);
		
		//Contains the communication log
		JPanel data3 = new JPanel();
		data3.setLayout(null);
		data3.setLocation(600,30);
		data3.setSize(300, 300);
		basepane.add(data3);

		//Contains the connect, home, and request data buttons
		JPanel buttons1 = new JPanel();
		buttons1.setLayout(null);
		buttons1.setLocation(30,400);
		buttons1.setSize(450, 200);
		basepane.add(buttons1);

		//Contains the stop, forward, backward, left, and right buttons
		JPanel buttons2 = new JPanel();
		buttons2.setLayout(null);
		buttons2.setLocation(500,400);
		buttons2.setSize(450, 200);
		basepane.add(buttons2);

		//sensor data display
		JLabel touch1label = new JLabel("Touch 1");
		touch1label.setSize(100,50);
		touch1label.setLocation(0,0);
		touch1label.setHorizontalAlignment(0);
		data2.add(touch1label);
		touch1data = new JTextField();
		touch1data.setSize(100,50);
		touch1data.setLocation(100,0);
		touch1data.setEditable(false);
		touch1data.setHorizontalAlignment(JTextField.RIGHT);
		touch1data.setText("0");
		data2.add(touch1data);

		//Display for touch sensor 2
		JLabel touch2label = new JLabel("Touch 2");
		touch2label.setSize(100,50);
		touch2label.setLocation(0,60);
		touch2label.setHorizontalAlignment(0);
		data2.add(touch2label);
		touch2data = new JTextField();
		touch2data.setSize(100,50);
		touch2data.setLocation(100,60);
		touch2data.setEditable(false);
		touch2data.setHorizontalAlignment(JTextField.RIGHT);
		touch2data.setText("0");
		data2.add(touch2data);

		//Display for the light sensor data
		JLabel lightlabel = new JLabel("Light");
		lightlabel.setSize(100,50);
		lightlabel.setLocation(0,120);
		lightlabel.setHorizontalAlignment(0);
		data2.add(lightlabel);
		lightdata = new JTextField();
		lightdata.setSize(100,50);
		lightdata.setLocation(100,120);
		lightdata.setEditable(false);
		lightdata.setHorizontalAlignment(JTextField.RIGHT);
		lightdata.setText("0");
		data2.add(lightdata);

		//Display for the sound sensor data
		JLabel soundlabel = new JLabel("Sound");
		soundlabel.setSize(100,50);
		soundlabel.setLocation(0,180);
		soundlabel.setHorizontalAlignment(0);
		data2.add(soundlabel);
		sounddata = new JTextField();
		sounddata.setSize(100,50);
		sounddata.setLocation(100,180);
		sounddata.setEditable(false);
		sounddata.setHorizontalAlignment(JTextField.RIGHT);
		sounddata.setText("0");
		data2.add(sounddata);

		//Display for the ultrasonic sensor data
		JLabel ultralabel = new JLabel("Ultrasonic");
		ultralabel.setSize(100,50);
		ultralabel.setLocation(0,240);
		ultralabel.setHorizontalAlignment(0);
		data2.add(ultralabel);
		ultradata = new JTextField();
		ultradata.setSize(100,50);
		ultradata.setLocation(100,240);
		ultradata.setEditable(false);
		ultradata.setHorizontalAlignment(JTextField.RIGHT);
		ultradata.setText("0");
		data2.add(ultradata);

		//command log
		commlog = new JTextArea();
		commlog.setSize(300,300);
		//command log display
		commlog.setLocation(0,0);
		commlog.setEditable(false);
		//commlog.setHorizontalAlignment(0);
		commlog.setText(" --Control Station online-- " + newline);
		JScrollPane commlogpane = new JScrollPane(commlog);
		commlogpane.setSize(300,300);
		commlogpane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		data3.add(commlogpane);

		//buttons
		JButton connect = new JButton("Connect");
		connect.setSize(120,60);
		connect.setLocation(0,0);
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				commlog.append("Connecting..." + newline);
				control.getController().connect();
				basepane.requestFocusInWindow();
			}
		});
		buttons1.add(connect);

		JButton home = new JButton("Home");
		home.setSize(120,60);
		home.setLocation(150,0);
		buttons1.add(home);

		JButton requestdata = new JButton("Request System Data");
		requestdata.setSize(180,40);
		requestdata.setLocation(40,80);
		requestdata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				control.getController().requestSystemStatusData();
				commlog.append("Requesting data..." + newline);
				basepane.requestFocusInWindow();
			}
		});
		buttons1.add(requestdata);

		JButton stop = new JButton("Stop");
		stop.setSize(70,50);
		stop.setLocation(10,20);
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				control.getController().stop();
				commlog.append("Stopped" + newline);
				basepane.requestFocusInWindow();
			}
		});
		buttons2.add(stop);

		JButton forward = new JButton(" ^ ");
		forward.setSize(50,50);
		forward.setLocation(60+padDisplaceX, 0+padDisplaceY);
		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				control.getController().moveForward(false, false);
				commlog.append("Moving forward..." + newline);
				basepane.requestFocusInWindow();
			}
		});
		buttons2.add(forward);

		JButton backward = new JButton(" v ");
		backward.setSize(50,50);
		backward.setLocation(60+padDisplaceX, 60+padDisplaceY);
		backward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				control.getController().moveBackward(false, false);
				commlog.append("Moving backward..." + newline);
				basepane.requestFocusInWindow();
			}
		});
		buttons2.add(backward);

		JButton left = new JButton(" < ");
		left.setSize(50,50);
		left.setLocation(0+padDisplaceX, 60+padDisplaceY);
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				control.getController().moveLeft();
				commlog.append("Turning left..." + newline);
				basepane.requestFocusInWindow();
			}
		});
		buttons2.add(left);

		JButton right = new JButton(" > ");
		right.setSize(50,50);
		right.setLocation(120+padDisplaceX, 60+padDisplaceY);
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				control.getController().moveRight();
				commlog.append("Turning right..." + newline);
				basepane.requestFocusInWindow();
			}
		});
		buttons2.add(right);

		basepane.addKeyListener(new KeyListener() { //Listener allowing the user to use keyboard buttons 
				//instead of on screen buttons
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
					if(!keysCurrentlyPressed.contains("up")) {
						boolean curveLeft = keysCurrentlyPressed.contains("left");
						boolean curveRight = keysCurrentlyPressed.contains("right");
						control.getController().moveForward(curveLeft, curveRight);
						commlog.append("Moving forward..." + newline);
						keysCurrentlyPressed.add("up");
					}
				}
				else if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
					if(!keysCurrentlyPressed.contains("down")) {
						boolean curveLeft = keysCurrentlyPressed.contains("left");
						boolean curveRight = keysCurrentlyPressed.contains("right");
						control.getController().moveBackward(curveLeft, curveRight);
						commlog.append("Moving backward..." + newline);
						keysCurrentlyPressed.add("down");
					}
				}
				else if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
					if(!keysCurrentlyPressed.contains("left")) {
						keysCurrentlyPressed.add("left");
						boolean curveForwardLeft = keysCurrentlyPressed.contains("up");
						boolean curveBackwardLeft = keysCurrentlyPressed.contains("down");
						if(curveForwardLeft && !curveBackwardLeft){
							control.getController().moveForward(true, false);
						}
						else if(!curveForwardLeft && !curveBackwardLeft){
							control.getController().moveBackward(true, false);
						}
						else {
							control.getController().moveLeft();
						}
						commlog.append("Turning left..." + newline);
						
					}
				}
				else if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
					if(!keysCurrentlyPressed.contains("right")) {
						keysCurrentlyPressed.add("right");
						boolean curveForwardRight = keysCurrentlyPressed.contains("up");
						boolean curveBackwardRight = keysCurrentlyPressed.contains("down");
						if(curveForwardRight && !curveBackwardRight){
							control.getController().moveForward(false, true);
						}
						else if(!curveForwardRight && curveBackwardRight){
							control.getController().moveBackward(false, true);
						}
						else {
							control.getController().moveRight();
						}
						commlog.append("Turning right..." + newline);
						
					}
				}
			}
			public void keyReleased(KeyEvent e) {
				String command = "";
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
					command = "up";
				}
				else if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
					command = "down";
				}
				else if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
					command = "left";
				}
				else if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
					command = "right";
				}
				
				keysCurrentlyPressed.remove(command);
				//System.out.println(keysCurrentlyPressed);
				control.getController().stop();
				commlog.append("Stopped" + newline);
			}
			public void keyTyped(KeyEvent e) {

			}
		});
		basepane.setFocusable(true);
		basepane.requestFocusInWindow();

		Timer timer = new Timer(50, new ActionListener() { //Timer which updates the sensor displays on screen
			public void actionPerformed(ActionEvent e) {
				sounddata.setText("" + control.getExtSensor().getSound().getValue());
				touch1data.setText("" + control.getExtSensor().getTouch().getValue());
				ultradata.setText("" + control.getExtSensor().getUltrasonic().getValue());
				lightdata.setText("" + control.getExtSensor().getLight().getValue());
			}
		});
		timer.start();
		
		Timer systemStatusTimer = new Timer(1000, new ActionListener() { //Timer which requests sensor updates
				//once per second
			public void actionPerformed(ActionEvent e) {
				control.getController().requestSystemStatusData();
				commlog.append("Requesting data..." + newline);
			}
		});
		systemStatusTimer.start();

		basepane.setOpaque(true);
		return basepane;
	}
}
