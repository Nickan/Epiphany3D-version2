package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.math.Dimension3D;
import com.nickan.epiphany.model.inventory.Inventory;

public class Player extends Character {
	public Inventory inventory;

	public Player(Vector3 position, Dimension3D dimension, float range, float speed) {
		super(position, dimension, range, speed);
		// TODO Auto-generated constructor stub
		inventory = new Inventory(statsHandler);
	}
	
	

}
