package com.nickan.epiphany.framework.finitestatemachine.messagingsystem;

public class Message {
	public enum MessageType { GET_LOCATION };
	
	public int senderId;
	public int receiverId;
	public float dispatchTime;
	public MessageType type;
	public Object extraInfo;
	
	public Message() {
		this.senderId = 0;
		this.receiverId = 0;
		this.dispatchTime = 0;
		this.type = MessageType.GET_LOCATION;
		this.extraInfo = null;
	}
	
	
}
