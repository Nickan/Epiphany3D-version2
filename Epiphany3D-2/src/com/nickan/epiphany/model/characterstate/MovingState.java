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
	
	private static final Vector3 tmpVec1 = new Vector3();
	private static final Vector3 tmpVec2 = new Vector3();
	
	@Override
	public void start(Character entity) {

	}

	@Override
	public void update(Character entity, float delta) {
		switch (currentMovement) {
		case FORWARD:
			tmpVec1.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec1.add(entity.getCamForwardVector());
			entity.steeringBehavior.setTargetPos(tmpVec1);
			break;
		case BACKWARD:
			tmpVec1.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec1.sub(entity.getCamForwardVector());
			entity.steeringBehavior.setTargetPos(tmpVec1);
			break;
		case LEFT:
			tmpVec1.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec1.sub(entity.getRightVector());
			entity.steeringBehavior.setTargetPos(tmpVec1);
			break;
		case RIGHT:
			tmpVec1.set(entity.getBoundingBox().getCenter()).y = 0; // No elevation
			tmpVec1.add(entity.getRightVector());
			entity.steeringBehavior.setTargetPos(tmpVec1);
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
				entity.setCurrentAction(Action.RUNNING);
			}
			return true;
		}
		
		return false;
	}

	public String toString() { return "Moving State"; }
	
	public static final BaseState<Character> getInstance() { return instance; }
	
}
