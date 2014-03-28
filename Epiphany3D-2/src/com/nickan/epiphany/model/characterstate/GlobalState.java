package com.nickan.epiphany.model.characterstate;

import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.MoveableEntity.Movement;

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
		// All the command that has PLAYER_ in front of it are player's command
		case PLAYER_ATTACK:
			int targetId = (Integer) message.extraInfo;
			entity.setTargetId(targetId);
			entity.charChangeState(AttackState.getInstance());
			return true;
		case PLAYER_MOVE:
			Movement movement = (Movement) message.extraInfo;
			entity.setCommandedMovement(movement);
			entity.charChangeState(MovingState.getInstance());
			return true;
			
		case ATTACK:
			MessageDispatcher.sendMessage(message.receiverId, message.senderId, 0, 
					MessageType.ATTACK_RESPONSE, entity.getBoundBox());
			return true;
			
		// The responses
		case ATTACK_RESPONSE:
			
			return true;
			
		default:
			break;
		}
		return false;
	}
	
	public String getStatus() {
		return "Global State";
	}
	
	public static BaseState<Character> getInstance() {
		return instance;
	}

}
