import java.util.Scanner;


public class ControlTester {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in); //replace the scanner with input from the control station once the control station is ready to be tested
		String input = scan.nextLine();
		int messageNumber = 0;
		
		while(!input.equals("disconnect")) {
			int calculatedChecksum = 0;
			for(int i = 3; i < input.length(); i++) {
				calculatedChecksum += (int)input.charAt(i);
			}
			int receivedChecksum = 256*((int)input.charAt(1)) + (int)input.charAt(2);
			if(!(input.charAt(0) == '#') || calculatedChecksum != receivedChecksum || !(input.charAt(3) == 'S')
					|| !(input.charAt(input.length() - 1) == (char)0)) { //check the message formatting
				String messageNum = "" + (char)((messageNumber/16777216)%256) + (char)((messageNumber/65536)%256) +
						(char)((messageNumber/256)%256) + (char)((messageNumber)%256);
				String param = "M" + "h" + input.substring(3, 8);
				String response = "R" + messageNum + param;
				int checksum = 0;
				for(int i = 0; i < response.length(); i++) {
					checksum += (int)response.charAt(i);
				}
				
			}
			System.out.println("Command acknowledged"); //replace "Command acknowledged" with the acknowledgment packet
			if(input.length() > 8) {
				switch(input.charAt(8)) { //the functions called in each case are responsible for checking the validity of the parameters.
				case 'A': OpcodeA(input);
				break;
				case 'B': OpcodeB(input);
				break;
				case 'C': OpcodeC(input);
				break;
				case 'D': OpcodeD(input);
				break;
				case 'E': OpcodeE(input);
				break;
				case 'F': OpcodeF(input);
				break;
				case 'G': OpcodeG(input);
				break;
				case 'H': OpcodeH(input);
				break;
				case 'I': OpcodeI(input);
				break;
				case 'J': OpcodeJ(input);
				break;
				case 'K': OpcodeK(input);
				break;
				case 'L': OpcodeL(input);
				break;
				case 'M': OpcodeM(input);
				break;
				case 'N': OpcodeM(input);
				break;
				case 'O': OpcodeM(input);
				break;
				case 'P': OpcodeM(input);
				break;
				case 'Q': OpcodeM(input);
				break;
				//add cases for each opcode
				default: System.out.println("Opcode not recognized"); // executes if the opcode character is not a valid opcode

				}
			}
			else {
				//if the input length is 0 then there is some sort of error, so send back an appropriate error message
			}
			input = scan.nextLine();
		}

	}
	public static void OpcodeA(String input) { // responds to the turn left command
		
	}
	public static void OpcodeB(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeC(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeD(String input) {
		//System.out.println("opcodea");
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeE(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeF(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeG(String input) {
		//System.out.println("opcodea");
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeH(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeI(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeJ(String input) {
		//System.out.println("opcodea");
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeK(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeL(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void OpcodeM(String input) {
		//System.out.println("opcodea");
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}

}
