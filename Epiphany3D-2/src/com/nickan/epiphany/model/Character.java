package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.StateMachine;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;


public class Character extends MoveableEntity {
	StateMachine<Character> stateMachine;
	
	public enum Action { IDLE, RUNNING, ATTACKING, DEAD };
	private Action currentAction = Action.IDLE;

	public Character(Vector3 position, Dimension3D dimension, float speed) {
		super(position, dimension, speed);
		stateMachine = new StateMachine<Character>(this);
	}
	
	public void update(float delta) {
		stateMachine.update(delta);
		super.update(delta);
	}
	
	@Override
	public void movementChanged(Movement movement) {
		if (movement == Movement.STOP) {
			currentAction = Action.IDLE;
		} else {
			currentAction = Action.RUNNING;
		}
	}
	
	/**
	 * Will be used for playing animation
	 * @return
	 */
	public Action getCurrentAction() {
		return currentAction;
	}

	public int getLife() {
		return 0;
	}
	
	
	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
