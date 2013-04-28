import java.util.ArrayList;


public class CommunicationLog {
	ArrayList<Entry> entries = new ArrayList<Entry>();
	public ArrayList<Entry> getLog() {
		
		return entries;
	}
	
	public void addEntry(Entry entry){
		entries.add(entry);
	}

	public void clearAll(){
		entries.clear();
	}
	//Returns all the entries in the Communication Log, 
	//which keeps track of all messages sent between the 
	//robot and base station	
}
