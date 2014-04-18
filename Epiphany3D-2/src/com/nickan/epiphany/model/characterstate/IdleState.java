package com.nickan.epiphany.model.characterstate;

import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.EntityManager;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Character.Action;

public class IdleState implements BaseState<Character> {
	private static final IdleState instance = new IdleState();
	
	private IdleState() { }

	@Override
	public void start(Character entity) {
		if (entity.getSteeringBehavior() != null) {
			entity.getSteeringBehavior().resetBehavior();
			entity.setCurrentAction(Action.IDLE);
		}
	}

	@Override
	public void update(Character entity, float delta) {
		Character targetChar = (Character) EntityManager.getEntity(entity.getTargetId());
		
		if (targetChar == null) {
			return;
		}
		
		// If the target character is in range, set to attack state
		if (entity.isInRange(targetChar.getPosition(), entity.getSightRange())) {
			entity.charChangeState(AttackState.getInstance());
		}
	}

	@Override
	public void exit(Character entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		
		return false;
	}
	
	public String toString() { return "Idle State"; }

	public static BaseState<Character> getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

}
