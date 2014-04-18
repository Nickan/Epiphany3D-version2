package com.nickan.epiphany.model.characterstate;

import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;
import com.nickan.epiphany.model.Character;

public class GlobalState implements BaseState<Character> {
	private static final GlobalState instance = new GlobalState();
	
	@Override
	public void start(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Character entity, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		switch (message.type) {
		case PLAYER_MOVE:
			entity.charChangeState(MovingState.getInstance());
			return false;
			
		case ATTACK:
			MessageDispatcher.sendMessage(message.receiverId, message.senderId, 0, 
					MessageType.ATTACK_RESPONSE, entity.getBoundingBox());
			
			return true;
			
		// The responses
		case ATTACK_RESPONSE:
		//	BoundBox tarBoundBox = (BoundBox) message.extraInfo;
		//	entity.setTargetBoundBox(tarBoundBox);
			
			return true;
			
		case IS_IN_RANGE:
			entity.setTargetId(message.senderId);
			entity.setTargetBoundingBox((OrientedBoundingBox) message.extraInfo);
			entity.charChangeState(AttackState.getInstance());
			return true;
			
		default:
			break;
		}
		return false;
	}
	
	public String toString() {
		return "Global State";
	}
	
	public static BaseState<Character> getInstance() {
		return instance;
	}

}
