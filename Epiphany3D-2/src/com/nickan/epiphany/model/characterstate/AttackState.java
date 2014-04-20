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
	private static final Vector3 tmpVec = new Vector3();
	private static final Vector3 tmpVec2 = new Vector3();

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
			
			entity.steeringBehavior.setTargetChar(targetChar);
			if (isAttacking(entity, targetChar)) {
				updateAttack(entity, delta);
				behavior.resetBehavior();
				
				// Face the target
				tmpVec.set(targetChar.getPosition()).sub(entity.getPosition());
				entity.setHeading(tmpVec.nor());
			} else {
				
				Vector3 tarPos = targetChar.getBoundingBox().getCenter();
				
				if (entity.isInRange(tarPos, entity.getSightRange())) {
					behavior.setTargetPos(tarPos);
					behavior.seekOn();
					behavior.avoidanceOn();
					entity.setCurrentAction(Action.RUNNING);
				} else {
					entity.charChangeState(IdleState.getInstance());
				}
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
	
	/**
	 * @param entity
	 * @param tarChar
	 * @return - If the entity is attacking, for now just use the range, it should be changed
	 * 				like if already attacking then the target gets out of range, it should continue
	 * 				finishing its attack, before seeking the target again.
	 */
	private boolean isAttacking(Character entity, Character targetChar) {
		boolean useSphereDetection = true; // If whether to use sphere or OBB for collision
		
		if (useSphereDetection) {
			OrientedBoundingBox entBox = entity.getBoundingBox();
			OrientedBoundingBox tarBox = targetChar.getBoundingBox();
			// No elevation
			tmpVec.set(entBox.getCenter()).y = 0;
			tmpVec2.set(tarBox.getCenter()).y = 0;
			float dist = tmpVec.dst2(tmpVec2);
			
			// For now, use the x-axis to dictate the radius of the entity
			float entityRadius = entBox.getDimension().x / 2;
			float targetRadius = tarBox.getDimension().x / 2;
			float range = entityRadius + targetRadius;
			
			return (dist < range * range);
		}
		
		/*
		 *  The problem with this OBB detection is that the radius of the center varies when
		 *  it rotates, so the radius varies which have undesired result
		 */
		return OrientedBoundingBox.collides(entity.getBoundingBox(), targetChar.getBoundingBox());
	}
	
	
	@Override
	public void exit(Character entity) {
		entity.setCurrentAction(Action.IDLE);
	}

	@Override
	public boolean handleMessage(Character entity, Message message) {
		return false;
	}
	
	@Override
	public String toString() { return "Attack State"; }

	public static BaseState<Character> getInstance() { return instance; }

}
