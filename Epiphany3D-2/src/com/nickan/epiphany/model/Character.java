package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseState;
import com.nickan.epiphany.framework.finitestatemachine.StateMachine;
import com.nickan.epiphany.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.epiphany.framework.math.OrientedBoundingBox;
import com.nickan.epiphany.framework.pathfinder.Node;
import com.nickan.epiphany.model.characterstate.GlobalState;
import com.nickan.epiphany.model.characterstate.IdleState;
import com.nickan.epiphany.model.inventory.Inventory;
import com.nickan.epiphany.screens.gamescreen.World;

/**
 * A generic character
 * @author Nickan
 *
 */
public class Character extends MoveableEntity {
	// Temporary, as I want to put abstraction to it
	public World world;
	
	// Can safely be declared as public, as it has its own interface
	public StatisticsHandler statsHandler;
	public Inventory inventory;
	
	int targetId;
	OrientedBoundingBox tarBoundingBox;
	
	public SteeringBehavior steeringBehavior;
	
	StateMachine<Character> charStateMachine;
	float attackDelay;
	float attackTimer;
	
	public enum Action { IDLE, RUNNING, ATTACKING, DEAD };
	private Action currentAction = Action.IDLE;
	
	//...
	private Vector3 accel = new Vector3();
	private Node currentNode = new Node();

	public Character(Vector3 pos, Vector3 dim, Vector3 rot, float mass, float maxForce, 
			float maxTurnRate, float maxSpeed, float sightRange) {
		super(pos, dim, rot, mass, maxForce, maxTurnRate, maxSpeed, sightRange);
		charStateMachine = new StateMachine<Character>(this, IdleState.getInstance(), GlobalState.getInstance());
		
		this.targetId = -1;
		setAttackDelay(1.0f);
		resetAttackTimer();
		statsHandler = new StatisticsHandler();
		inventory = new Inventory(statsHandler);
		steeringBehavior = new SteeringBehavior(this);
	}
	
	public void update(float delta) {
		charStateMachine.update(delta);
		
		Vector3 steeringForce = steeringBehavior.calcSteeringForce();
		
		// Setting the acceleration
		// Acceleration = Force / Mass
		accel.set(steeringForce).scl(1.0f / mass);
		
		// Applying an instant break when the force goes zero, just for now
		if (steeringForce.len2() <= 0) {
			velocity.set(Vector3.Zero);
		}
		velocity.add(accel.scl(delta));

		// Making sure that the length of the velocity doesn't exceed to the set maxSpeed
		if (velocity.len2() >= maxSpeed * maxSpeed) {
			velocity.nor().scl(maxSpeed);
		}
		
		// Update the position
		position.add(velocity);
		limitMovementInsideWorld();
		
		// Needs to have a copy of the velocity, as nor() changes the value of vector
		// which defeats the purpose of the setting up the velocity
		tempVec1.set(velocity);
		tempVec1.nor();
		
		// Update the heading if the velocity is not zero
		if (tempVec1.len2() > 0.0001f) {
			heading.set(tempVec1.nor());
		}
		
		super.update(delta);
	}
	
	/**
	 * Limits the position of the character inside the world
	 */
	private void limitMovementInsideWorld() {
		steeringBehavior.setInvertSteeringX(false);	// Reset the inversion of the steering force x
		
		// Movement about x-axis
		if (position.x < obb.getDimension().x) {
			position.x = obb.getDimension().x;
			
			// Invert the steering force about x-axis in local space
			steeringBehavior.setInvertSteeringX(true);
		}
		
		// Movement about z-axis
		if (position.z < obb.getDimension().z) {
			position.z = obb.getDimension().z;
			
			// Invert the steering force about x-axis in local space
			steeringBehavior.setInvertSteeringX(true);
		}
	}
	
	
	public int getLife() { return 0; }
	
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
	
	public void setCurrentNode(int nodeX, int nodeY) { currentNode.set(nodeX, nodeY); }
	public Node getCurrentNode() { return currentNode; }
	
	public SteeringBehavior getSteeringBehavior() { return steeringBehavior; }
	
	@Override
	public boolean handleMessage(Message message) { return charStateMachine.handleMessage(message); }

}
