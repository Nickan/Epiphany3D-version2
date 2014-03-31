package com.nickan.epiphany.model;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.StateMachine;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
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
	BoundingBox tarBoundingBox;
	
	StateMachine<Character> charStateMachine;
	float attackDelay;
	float attackTimer;
	
	public enum Action { IDLE, RUNNING, ATTACKING, DEAD };
	private Action currentAction = Action.IDLE;

	public Character(BoundingBox boundingBox, float sightRange, float speed) {
		super(boundingBox, sightRange, speed);
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
	
	public void setTargetBoundingBox(BoundingBox tarBoundingBox) { this.tarBoundingBox = tarBoundingBox; }
	public BoundingBox getTargetBoundingBox() { return tarBoundingBox; }
	
	public void charChangeState(BaseState<Character> state) { charStateMachine.changeState(state); }
	
	@Override
	public boolean handleMessage(Message message) {
		return charStateMachine.handleMessage(message);
	}

}
