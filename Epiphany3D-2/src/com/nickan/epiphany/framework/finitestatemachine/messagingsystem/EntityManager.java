package com.nickan.epiphany.framework.finitestatemachine.messagingsystem;

import com.nickan.epiphany.framework.finitestatemachine.BaseEntity;

/**
 * 
 * @author Nickan
 *
 */
public class EntityManager {
	// Limited quantity for now, to be changed later if needed
	private static final int QUANTITY = 300;
	private static BaseEntity[] entityList = new BaseEntity[QUANTITY];

	private EntityManager() {
		clear();
	}

	public static int getAssignedId(BaseEntity entity) {
		for (int i = 0; i < entityList.length; ++i) {
			if (entityList[i] == null) {
				entityList[i] = entity;
				return i;
			}
		}
		return -1;
	}

	public static  boolean deleteEntity(int id) {
		if (entityList[id] != null) {
			entityList[id] = null;
			return true;
		}
		return false;
	}

	public static BaseEntity getEntity(int id) {
		return entityList[id];
	}

	public static void clear() {
		for (int i = 0; i < entityList.length; ++i)
			entityList[i] = null;
	}

}
