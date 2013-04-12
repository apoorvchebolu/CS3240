import java.util.ArrayList;


public class MessageHolder {
	private ArrayList<Message> messageList;
	
	public MessageHolder() {
		messageList = new ArrayList<Message>();
	}
	
	public void addMessage(Message m) {
		messageList.add(m);
	}
	public boolean removeFirstMessage() {
		if(messageList.size() > 0) {
			messageList.remove(0);
			return true;
		}
		return false;
	}
	public boolean removeLastMessage() {
		if(messageList.size() > 0) {
			messageList.remove(messageList.size()  - 1);
			return true;
		}
		return false;
	}
}
