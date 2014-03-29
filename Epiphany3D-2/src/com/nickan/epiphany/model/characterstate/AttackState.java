package com.nickan.epiphany.model.characterstate;

import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.epiphany.framework.math.BoundBox;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Character.Action;

public class AttackState implements BaseState<Character> {
	private static final AttackState instance = new AttackState();

	@Override
	public void start(Character entity) {
		MessageDispatcher.sendMessage(entity.getId(), entity.getTargetId(), 0, MessageType.ATTACK, null);
	}

	@Override
	public void update(Character entity, float delta) {
		BoundBox tarBound = entity.getTargetBoundBox();
		// Meaning the coordinates of the enemy is set
		if (tarBound != null) {
			// Check if the target has not been collided
			if (!entity.isInRange(tarBound.getCenter(), tarBound.dimension.width / 2 + 
					entity.getDimension().width / 2)) {
				entity.seek(tarBound.getCenter(), delta);
				entity.setCurrentAction(Action.RUNNING);
			} else {
				updateAttack(entity, delta);
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
			//...
			System.out.println("Attack!");
		}
	}

	@Override
	public void exit(Character entity) {
		entity.setTargetBoundBox(null);
		entity.setTargetId(-1);
		entity.setCurrentAction(Action.IDLE);
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		return false;
	}
	
	@Override
	public String getStatus() {
		return "Attack State";
	}

	public static BaseState<Character> getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

}
