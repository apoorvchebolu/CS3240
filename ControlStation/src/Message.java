public class Message {
	private String messageType;
	private String messageContent;
	private String readableMessageContent;

	public Message() {
		messageType = "";
		messageContent = "";
	}

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

	public String getReadableMessageContent() {
		return readableMessageContent;
	}

	public void setMessageType(String type) {
		messageType = type;
	}

	public void setMessageContent(String content) {
		messageContent = content;
	}

	public void setReadableMessageContent(String content) {
		readableMessageContent = content;
	}
}
