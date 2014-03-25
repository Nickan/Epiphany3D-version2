package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseEntity;

public abstract class Entity extends BaseEntity {
	Vector3 position;
	Dimension3D dimension;
	
	public Entity(Vector3 position, Dimension3D dimension) {
		this.position = position;
		this.dimension = dimension;
	}
	
	public final Vector3 getPosition() {
		return position;
	}
	
	public final Dimension3D getDimension() {
		return dimension;
	}
}
