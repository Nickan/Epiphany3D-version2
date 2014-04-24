package com.nickan.epiphany.model.characterstate;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Character.Action;

/**
 * For the implementation of the player movement
 * @author Nickan
 *
 */
public class MovingState implements BaseState<Character> {
	private static final BaseState<Character> instance = new MovingState();
	
	// For now, this is for the player
	public enum Movement { FORWARD, BACKWARD, LEFT, RIGHT, STOP };
	private Movement currentMovement = Movement.STOP;
	
	private static final Vector3 tmpVec = new Vector3();
	
	@Override
	public void start(Character entity) {

	}

	@Override
	public void update(Character entity, float delta) {
		switch (currentMovement) {
		case FORWARD:
			tmpVec.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec.add(entity.getCamForwardVector());
			entity.steeringBehavior.setTargetPos(tmpVec);
			break;
		case BACKWARD:
			tmpVec.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec.sub(entity.getCamForwardVector());
			entity.steeringBehavior.setTargetPos(tmpVec);
			break;
		case LEFT:
			tmpVec.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec.sub(entity.getRightVector());
			entity.steeringBehavior.setTargetPos(tmpVec);
			break;
		case RIGHT:
			tmpVec.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec.add(entity.getRightVector());
			entity.steeringBehavior.setTargetPos(tmpVec);
			break;
		default:
			break;

		}
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		if (message.type == MessageType.PLAYER_MOVE) {
			currentMovement = ((Movement) message.extraInfo);
			
			if (currentMovement == Movement.STOP) {
				entity.charChangeState(IdleState.getInstance());
			} else {
				entity.steeringBehavior.seekOn();
				entity.steeringBehavior.avoidanceOn();
				entity.setTargetId(-1);
				entity.setCurrentAction(Action.RUNNING);
			}
			return true;
		}
		
		return false;
	}

	public String toString() { return "Moving State"; }
	
	public static final BaseState<Character> getInstance() { return instance; }
	
}
