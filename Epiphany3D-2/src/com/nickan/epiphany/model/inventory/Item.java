package com.nickan.epiphany.model.inventory;

import com.nickan.epiphany.model.StatisticsHandler;

/**
 * Base class for item
 * @author Nickan
 *
 */
public abstract class Item {
	public enum ItemClass { WEARABLE, CONSUMABLE };
	private final ItemClass itemClass;
	
	public Item(ItemClass itemClass) {
		this.itemClass = itemClass;
	}
	
	public abstract void use(StatisticsHandler statsHandler);
	
	public final ItemClass getItemClass() {
		return itemClass;
	}
}
