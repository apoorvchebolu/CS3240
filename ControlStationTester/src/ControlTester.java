import java.util.Scanner;


public class ControlTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in); //replace the scanner with input from the control station once the control station is ready to be tested
		String input = scan.nextLine();
		while(!input.equals("disconnect")) {
			System.out.println("Command acknowledged"); //replace "Command acknowledged" with the acknowledgment packet
			if(input.length() > 0) {
				switch(input.charAt(0)) { //the functions called in each case are responsible for checking the validity of the parameters.
					case 'a': Opcodea(input);
						break;
					case 'b': Opcodeb(input);
						break;
					case 'c': Opcodec(input);
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
	public static void Opcodea(String input) {
		//System.out.println("opcodea");
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void Opcodeb(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	public static void Opcodec(String input) {
		//do something with the input.  Any messages that would be sent back to the control station should be printed to the console instead until we have a control station to test.
	}
	//add more functions, 1 for each opcode

}
