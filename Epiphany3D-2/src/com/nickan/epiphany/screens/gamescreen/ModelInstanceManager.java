package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Manages the array of model instances to be rendered on the screen
 * @author Nickan
 *
 */
public class ModelInstanceManager {
	private Array<ModelInstance> idleInstances;
	private float drawDistance;
	private Vector3 tempPos;

	public ModelInstanceManager() {
		idleInstances = new Array<ModelInstance>();
		tempPos = new Vector3();
		drawDistance = 15;
	}
	
	/**
	 * Will store the instances that is out of range based on the distance from the base position and
	 * will bring back the idle instances when they are in range.
	 * @param instances
	 * @param basePosition
	 */
	public void update(Array<ModelInstance> instances, Vector3 basePosition) {
		for (ModelInstance instance: instances) {
			instance.transform.getTranslation(tempPos);

			// If the ModelInstance is far away from the set drawDistance
			if (basePosition.dst2(tempPos) > drawDistance * drawDistance) {
				idleInstances.add(instance);
				instances.removeValue(instance, true);
				continue;
			}
		}

		for (ModelInstance instance: idleInstances) {
			instance.transform.getTranslation(tempPos);
			// If the ModelInstance is near the drawDistance
			if (basePosition.dst2(tempPos) < drawDistance * drawDistance) {
				instances.add(instance);
				idleInstances.removeValue(instance, true);
				continue;
			}
		}
	}
	
	public void setDrawDistance(float drawDistance) {
		this.drawDistance = drawDistance;
	}
	
}
