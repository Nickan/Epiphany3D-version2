package com.nickan.epiphany.model;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.nickan.epiphany.model.inventory.Inventory;

public class Player extends Character {
	public Inventory inventory;

	public Player(BoundingBox boundingBox, float range, float speed) {
		super(boundingBox, range, speed);
		// TODO Auto-generated constructor stub
		inventory = new Inventory(statsHandler);
	}
	
	

}
