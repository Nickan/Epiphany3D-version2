package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.model.inventory.Inventory;

public class Player extends Character {
	public Inventory inventory;
	
	public Player(Vector3 position, Vector3 dimension, Vector3 rotation,
			float sightRange, float speed) {
		super(position, dimension, rotation, sightRange, speed);
		// TODO Auto-generated constructor stub
	}

}
