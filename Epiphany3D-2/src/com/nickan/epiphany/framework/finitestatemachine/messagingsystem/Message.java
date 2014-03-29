package com.nickan.epiphany.framework.finitestatemachine.messagingsystem;

public class Message {
	/** A set of message types include the player's command */
	public enum MessageType { /** Player's command */ PLAYER_MOVE, 
		ATTACK, ATTACK_RESPONSE, IS_IN_RANGE,
		};
	
	public int senderId;
	public int receiverId;
	public float dispatchTime;
	public MessageType type;
	public Object extraInfo;
	
	public Message() {
		this.senderId = 0;
		this.receiverId = 0;
		this.dispatchTime = 0;
		this.type = null;
		this.extraInfo = null;
	}
	
}
