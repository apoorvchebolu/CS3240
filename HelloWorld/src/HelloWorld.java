import lejos.nxt.*;

public class HelloWorld {

	public static void main(String[] args){
		
		//test edit - Sam Feb4
		
		System.out.println("Hello World");
		Button.waitForPress();
		Motor.A.forward();
		Button.waitForPress();
 		Motor.A.backward();
   		Button.waitForPress();
		Motor.A.stop();
		System.out.println("Finished");
		Button.waitForPress();
		
	}
}