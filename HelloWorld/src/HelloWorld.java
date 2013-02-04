import lejos.nxt.*;

public class HelloWorld {

	public static void main(String[] args){
		
		//test edit - Sam Feb4
		
		System.out.println("Hello World");
		Button.waitForPress();
		Motor.A.forward();
		Motor.C.backward();
		Button.waitForPress();
 		Motor.A.backward();
 		Motor.C.forward();
   		Button.waitForPress();
		Motor.A.stop();
		Motor.C.stop();
		System.out.println("Finished");
		Button.waitForPress();
		
	}
}