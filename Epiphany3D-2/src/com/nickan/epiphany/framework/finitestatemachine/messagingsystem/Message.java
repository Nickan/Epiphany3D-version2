package com.nickan.epiphany.framework.finitestatemachine.messagingsystem;

public class Message {
	/** A set of message types include the player's command */
	public enum MessageType { /** Player's command */ PLAYER_ATTACK, PLAYER_MOVE, 
		ATTACK, ATTACK_RESPONSE,
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
		this.type = MessageType.PLAYER_ATTACK;
		this.extraInfo = null;
	}
	
}
