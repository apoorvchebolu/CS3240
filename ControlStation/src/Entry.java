
public class Entry {
	
	String fullEntry;
	boolean sentStatus;
	String opCode;
	public Entry (String entry, boolean status, String oc) {
		fullEntry = entry;
		sentStatus = status;
		opCode = oc;
		
		
	}
	public String getEntry() {
		return fullEntry;
	}
	//Returns a string value of an entry in the communication log
	
	public void setEntry(String entry){
		fullEntry = entry;
	}
	//Sets the value of an entry in the communication log

	public boolean getSentStatus(){
		return sentStatus;
	}
		//true = means message was sent
		//false = means message was received 
	
	public void setSentStatus(boolean status){
		sentStatus = status;
	}
	//sets boolean value for status
	
	public String getCode() {
		return opCode;
	}
	//Returns the opcode for an entry.
	
	public void setCode(String oc){
		opCode = oc;
	}
	//Sets opcode for an entry.
}
