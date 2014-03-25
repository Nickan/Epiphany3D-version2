package com.nickan.epiphany.gamescreen;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.Character.Action;

/**
 * Manages the animation of the character based on its current action, decoupled a lot of dependencies
 * @author Nickan
 *
 */
public class AnimationHandler {
	private AnimationController aniController;
	private Character character;
	
	private Action currentAction;
	private static final Vector3 aniForwardVector = new Vector3();
	
	/** For calculation of the matrix, I might change this later */
	private final static Matrix4 aniTempMatrix = new Matrix4();

	/** 
	 * Default play speed, maybe based on blender's play speed. No reason to change or modify it. 
	 * I will work based on it 
	 */
	private static final float DEFAULT_FPS = 24.0f;

	/** Defines the animation play time will be played in one second */
	private final float attackAnimationPlaySpeed;
	private final float standingPlaySpeed;
	private final float runningPlaySpeed;

	/** Make the scale global for now... To be changed later if needed */
	public float aniScale = 1f;

	private final static float DEFAULT_ATTACK_SPEED = 1.0f;
	/** The time to start playing the attack animation to synchronize it */
	private float attackPlayTime;
	public float attackPlaySpeed;
	private boolean firstAttack = true;
	private boolean attackUpdated = false;

	public AnimationHandler(Character character, AnimationController aniController, float standingFrameNum, 
			float runningFrameNum, float attackingFrameNum) {
		this.character = character;
		this.aniController = aniController;

		standingPlaySpeed = standingFrameNum / DEFAULT_FPS;
		runningPlaySpeed = (runningFrameNum / DEFAULT_FPS) * 2;
		attackAnimationPlaySpeed = attackingFrameNum / DEFAULT_FPS;
	}
	
	public void update(float delta) {
		// If the character's current action has changed
		if (character.getCurrentAction() != currentAction) {
			changeAnimation(character.getCurrentAction());
			
			// Set the current action to switch of the setting of the new animation
			currentAction = character.getCurrentAction();
		}
		
		if (currentAction == Action.ATTACKING)
			handleAttackUpdate(0);

		updateAnimationMatrix();
		aniController.update(delta);
	}
	
	private void changeAnimation(Action action) {
		// Change the animation corresponding to the new action
		switch (action) {
		case ATTACKING:
			aniController.setAnimation("Attacking");
			aniController.update(1000);
			firstAttack = true;
			attackUpdated = false;
			break;
		case IDLE:
			aniController.setAnimation("Standing", -1, standingPlaySpeed, null);
			break;
		case RUNNING:
			aniController.setAnimation("Running", -1, runningPlaySpeed, null);
			firstAttack = true;
			break;
		default:
			break;
		}
	}


	/**
	 * Handles the start of playing attack animation.
	 * @param attackTime - The attack timer of the character
	 */
	private void handleAttackUpdate(float attackTime) {
		// Should know if that's the first time the animation will be played, to show believable attack animation
		if (firstAttack) {
			if (attackTime >= attackPlayTime) {
				firstAttack = false;
			}
		} else {
			updateContinousAttack(attackTime);
		}

	}

	/**
	 * Will continue updating the attack animation after the attack time goes back to zero, to show the sword or any weapon
	 * to be brought back from the attack stance or starting attack stance.
	 * @param attackTime - The attack timer of the character
	 */
	private void updateContinousAttack(float attackTime) {
		if (!attackUpdated) {
			if (attackTime >= attackPlayTime) {
				attackUpdated = true;
				playAttackAnimation();
			}
		} else {
			if (attackTime < attackPlayTime) {
				attackUpdated = false;
			}
		}
	}

	private void playAttackAnimation() {
		// Finish the currently updating attack to reset it
	//	aniController.update(1000);
		aniController.setAnimation("Standing");
		aniController.update(1000);
		aniController.setAnimation("Attacking", 1, attackPlaySpeed, null);
	//	aniController.animate("Attacking", 1, attackPlaySpeed, null, 0);
		
		//...
		System.out.println("Attack!!!");
	}

	/**
	 * Set the attack counter value and animation to make them synchronize to attack delay of the character.
	 * If the attack delay is greater than one, the difference of one to the attack delay will just make the animation wait to attack.
	 * otherwise the delay time to play the attack animation is just half the attack delay.
	 */
	private void setAttackAnimationSpeed(float attackDelay) {
		float attackTimeToLandHit = attackDelay / 2f;

		/*
		 * Set the default attack delay of the animation to one second, to prevent boring very slow attack animation,
		 * but if the attack delay is lower than one second, set the attack delay of the animation according to it.
		 */
		if (attackDelay > DEFAULT_ATTACK_SPEED) {	
			attackPlayTime = attackDelay - DEFAULT_ATTACK_SPEED / 2;
			attackPlaySpeed = attackAnimationPlaySpeed;
		} else {
			attackPlayTime = attackTimeToLandHit;
			attackPlaySpeed = attackAnimationPlaySpeed / attackDelay;
		}
	}

	/**
	 * Updates and manipulates the ModelInstance's matrix to be passed here
	 *
	 * @param position - Position of the character
	 * @param rotation - Rotation of the character
	 * @param aniMatrix - The matrix from ModelInstance
	 */
	private void updateAnimationMatrix() {
		Matrix4 aniMatrix = aniController.target.transform;
		
		Vector3 position = character.getPosition();
		// Set the position
		aniMatrix.idt();
		aniMatrix.translate(position.x, position.y, position.z);

		// Set the rotation
		aniTempMatrix.idt();
		
		
		switch (character.getMovement()) {
		case FORWARD: aniForwardVector.set(character.getForwardVector());
			break;
		case BACKWARD: aniForwardVector.set(character.getForwardVector()).scl(-1);
			break;
		case LEFT: aniForwardVector.set(character.getRightVector()).scl(-1);
			break;
		case RIGHT: aniForwardVector.set(character.getRightVector());
			break;
		default: aniForwardVector.set(character.getForwardVector());
			break;
		}
		
		// Get the backward value of the set forward vector
		aniTempMatrix.setToLookAt(aniForwardVector.scl(-1), Vector3.Y);
		aniTempMatrix.inv();

		// Apply the value to the Matrix of the animation
		aniMatrix.mul(aniTempMatrix);

		// Set the scale
		aniTempMatrix.idt();
		aniTempMatrix.scl(aniScale);
		
		// Apply the value to the Matrix of the animations
		aniMatrix.mul(aniTempMatrix);
	}
	
	
	/*
	private void playDead() {	
		aniController.setAnimation("Stumbling", 1, 1, new AnimationListener() {

			@Override
			public void onEnd(AnimationDesc animation) {
				// TODO Auto-generated method stub
				float timePerFrame = 1f / DEFAULT_FPS;
				aniController.update(1000);
				aniController.animate("Stumbling", 0);
				aniController.update(timePerFrame * 14);
			}

			@Override
			public void onLoop(AnimationDesc animation) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	*/
}