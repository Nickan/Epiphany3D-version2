package com.nickan.epiphany.model;

import com.badlogic.gdx.math.Vector3;
import com.nickan.epiphany.framework.finitestatemachine.BaseEntity;
import com.nickan.epiphany.framework.math.BoundBox;
import com.nickan.epiphany.framework.math.Dimension3D;

public abstract class Entity extends BaseEntity {
	BoundBox boundBox;
	
	public Entity(Vector3 position, Dimension3D dimension) {
		boundBox = new BoundBox(position, dimension);
	}
	
	public Vector3 getPosition() {
		return boundBox.position;
	}
	
	public Dimension3D getDimension() {
		return boundBox.dimension;
	}
	
	public BoundBox getBoundBox() {
		return boundBox;
	}
	
	public Vector3 getCenter() {
		return boundBox.getCenter();
	}
}
