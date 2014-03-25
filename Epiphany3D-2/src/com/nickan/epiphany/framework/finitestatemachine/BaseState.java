package com.nickan.epiphany.framework.finitestatemachine;

import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;

public abstract class BaseState<EntityType> {
	EntityType entityType;
	
	private BaseState() { }
	
	public abstract void start(EntityType entity);
	
	public abstract void update(EntityType entity);
	
	public abstract void exit(EntityType entity);
	
	public abstract boolean handleTelegram(Message message);
	
	public abstract BaseState<EntityType> getInstance();
	
}
