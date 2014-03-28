package com.nickan.epiphany.model.characterstate;

import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Character.Action;
import com.nickan.epiphany.model.MoveableEntity.Movement;

/**
 * Handles the player's movement command
 * 
 * @author Nickan
 *
 */
public class MovingState implements BaseState<Character> {
	private static final MovingState instance = new MovingState();
	
	@Override
	public void start(Character entity) {
		
	}

	@Override
	public void update(Character entity, float delta) {
		// The movement of the character is not the player commanded it to do
		Movement commandedMovement = entity.getCommandedMovement();
		if (commandedMovement != entity.getCurrentMovement()) {
			// Set the current movement as the commanded movement to know it has already been handled
			entity.setCurrentMovement(entity.getCommandedMovement());
			
			if (commandedMovement != Movement.STOP) {
				entity.setForwardSpeed(entity.getMaxSpeed());
				entity.forwardVectorToCamDirection();
				entity.setCurrentAction(Action.RUNNING);
			}
			
			// Only one direction will be processed at a time
			switch (entity.getCommandedMovement()) {
			case FORWARD:
				break;
			case BACKWARD:
				// Just reverse forward vector
				entity.setForwardVector(entity.getForwardVector().scl(-1));
				break;
			case LEFT:
				entity.setForwardVector(entity.getRightVector().scl(-1));
				break;
			case RIGHT:
				entity.setForwardVector(entity.getRightVector());
				break;
			case STOP:
				entity.setForwardSpeed(0);
				entity.setCurrentAction(Action.IDLE);
				break;
			default:
				break;
			}

		}
		
		
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStatus() {
		return "Moving State";
	}
	
	public static final BaseState<Character> getInstance() {
		return instance;
	}

}
