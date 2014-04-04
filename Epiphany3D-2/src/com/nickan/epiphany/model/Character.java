package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.StateMachine;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;
import com.nickan.epiphany.model.characterstate.GlobalState;
import com.nickan.epiphany.model.characterstate.IdleState;

/**
 * Knows how to attack and set the values for animation
 * @author Nickan
 *
 */
public class Character extends MoveableEntity {
	// Can safely be declared as public, as it has its own interface
	StatisticsHandler statsHandler;
	
	int targetId;
	OrientedBoundingBox tarBoundingBox;
	
	StateMachine<Character> charStateMachine;
	float attackDelay;
	float attackTimer;
	
	public enum Action { IDLE, RUNNING, ATTACKING, DEAD };
	private Action currentAction = Action.IDLE;

	public Character(Vector3 position, Vector3 dimension, Vector3 rotation, float sightRange, float speed) {
		super(position, dimension, rotation, sightRange, speed);
		charStateMachine = new StateMachine<Character>(this, IdleState.getInstance(), GlobalState.getInstance());
		
		this.targetId = -1;
		setAttackDelay(1.0f);
		resetAttackTimer();
		statsHandler = new StatisticsHandler();
	}
	
	public void update(float delta) {
		charStateMachine.update(delta);
		super.update(delta);
	}
	
	public int getLife() {
		return 0;
	}
	
	public void setTargetId(int targetId) { this.targetId = targetId; }
	public int getTargetId() { return targetId; }
	
	public void setAttackDelay(float attackDelay) { this.attackDelay = attackDelay; }
	public float getAttackDelay() { return attackDelay; }
	
	public void incAttackTimer(float delta) { attackTimer += delta; }
	public float getAttackTimer() { return attackTimer; }
	public void resetAttackTimer() { attackTimer = 0; }
	
	public void setCurrentAction(Action currentAction) { this.currentAction = currentAction; }
	public Action getCurrentAction() { return currentAction; }
	
	public void setTargetBoundingBox(OrientedBoundingBox tarBoundingBox) { this.tarBoundingBox = tarBoundingBox; }
	public OrientedBoundingBox getTargetBoundingBox() { return tarBoundingBox; }
	
	public void charChangeState(BaseState<Character> state) { charStateMachine.changeState(state); }
	
	@Override
	public boolean handleMessage(Message message) {
		return charStateMachine.handleMessage(message);
	}

}
