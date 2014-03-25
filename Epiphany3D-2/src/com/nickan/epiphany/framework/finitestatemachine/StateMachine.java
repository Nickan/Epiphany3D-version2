package com.nickan.epiphany.framework.finitestatemachine;

import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;


public class StateMachine<EntityType> {
	EntityType entity;
	BaseState<EntityType> currentState = null;

	public StateMachine(EntityType entity) {
		this.entity = entity;
	}
	
	public void update(float delta) {
		if (currentState != null) {
			currentState.update(entity);
		}
	}
	
	public void changeState(BaseState<EntityType> state) {
		if (currentState != null) {
			currentState.exit(entity);
		}
		currentState = state;
	}
	
	public boolean handleTelegram(Message message) {
		// For now, return the current state's message handling capability
		return currentState.handleTelegram(message);
	}
}
