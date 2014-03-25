package com.nickan.epiphany.framework.finitestatemachine;

import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.EntityManager;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;

/**
 * Should be the ancestor to implement the FSM. Automatically creates an ID for the entity whenever it is inherited
 * @author Nickan
 *
 */
public abstract class BaseEntity {
	protected int id;
	
	public BaseEntity() {
		this.id = EntityManager.getAssignedId(this);
	}
	
	public final int getId() {
		return id;
	}
	
	public abstract boolean handleMessage(Message message);
}
