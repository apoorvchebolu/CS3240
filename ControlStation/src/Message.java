
public class Message {
	private String messageType;
	private String messageContent;
	
	public Message(String messageType, String messageContent) {
		this.messageType = messageType;
		this.messageContent = messageContent;
	}
	public String getTypeOfMessage() {
		return messageType;
	}
	public String getMessageContent() {
		return messageContent;
	}
}
