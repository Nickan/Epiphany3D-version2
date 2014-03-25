package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.StateMachine;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;


public class Character extends MoveableEntity {
	StateMachine<Character> stateMachine;
	
	public enum Action { IDLE, RUNNING, ATTACKING, DEAD };
	private Action currentAction = Action.IDLE;

	public Character(Vector3 position, Vector3 rotation, Dimension3D dimension, float speed) {
		super(position, rotation, dimension, speed);
		stateMachine = new StateMachine<Character>(this);
	}
	
	public void update(float delta) {
		stateMachine.update(delta);
		super.update(delta);
	}
	
	/**
	 * Will be used for playing animation
	 * @return
	 */
	public Action getCurrentAction() {
		return currentAction;
	}

	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getLife() {
		return 0;
	}

}
