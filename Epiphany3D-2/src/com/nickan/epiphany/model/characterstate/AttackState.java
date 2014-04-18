package com.nickan.epiphany.model.characterstate;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.EntityManager;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Character.Action;
import com.nickan.epiphany.model.SteeringBehavior;

public class AttackState implements BaseState<Character> {
	private static final AttackState instance = new AttackState();

	@Override
	public void start(Character entity) {
	//	MessageDispatcher.sendMessage(entity.getId(), entity.getTargetId(), 0, MessageType.ATTACK, null);
		
		SteeringBehavior behavior = entity.getSteeringBehavior();
		Character targetChar = (Character) EntityManager.getEntity(entity.getTargetId());
		if (targetChar != null) {
			behavior.setTargetPos(targetChar.getBoundingBox().getCenter());
			behavior.seekOn();
			behavior.avoidanceOn();
			entity.setCurrentAction(Action.RUNNING);
		}
		
	}

	@Override
	public void update(Character entity, float delta) {
		Character targetChar = (Character) EntityManager.getEntity(entity.getTargetId());
		if (targetChar != null) {
			SteeringBehavior behavior = entity.getSteeringBehavior();
		//	if (entity.isInRange(targetChar.getPosition(), 1)) {
			if (OrientedBoundingBox.collides(entity.getBoundingBox(), targetChar.getBoundingBox())) {
				updateAttack(entity, delta);
				behavior.resetBehavior();
				
				// Face the target
				Vector3 faceVector = new Vector3(targetChar.getPosition()).sub(entity.getPosition());
				entity.setHeading(faceVector.nor());
			} else {
				behavior.setTargetPos(targetChar.getBoundingBox().getCenter());
				behavior.seekOn();
				behavior.avoidanceOn();
				entity.setCurrentAction(Action.RUNNING);
			}
		}
		
	}
	
	private void updateAttack(Character entity, float delta) {
		if (entity.getCurrentAction() != Action.ATTACKING) {
			entity.setCurrentAction(Action.ATTACKING);
			entity.resetAttackTimer();
		}
		
		entity.incAttackTimer(delta);
		// If the attack timer is done
		if (entity.getAttackTimer() >= entity.getAttackDelay()) {
			
			// Decrease the value of the attack timer by the attack delay so that the result
			// will still be added to the current attack timer
			entity.incAttackTimer(-entity.getAttackDelay());
		}
	}

	@Override
	public void exit(Character entity) {
		entity.setTargetBoundingBox(null);
		entity.setTargetId(-1);
		entity.setCurrentAction(Action.IDLE);
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		switch (message.type) {
		case COLLISION_DETECTED_AHEAD:
			
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public String toString() { return "Attack State"; }

	public static BaseState<Character> getInstance() { return instance; }

}
