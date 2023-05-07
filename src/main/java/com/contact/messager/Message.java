package com.contact.messager;

public class Message {
	
	public String content;
	public String messagetype;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessagetype() {
		return messagetype;
	}
	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}
	public Message(String content, String messagetype) {
		super();
		this.content = content;
		this.messagetype = messagetype;
	}
	public Message() {
		
	}
}
