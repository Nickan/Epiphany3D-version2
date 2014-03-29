package com.nickan.epiphany.model.inventory;

import com.nickan.epiphany.model.StatisticsHandler;
import com.nickan.epiphany.model.inventory.Consumable.ConsumableType;
import com.nickan.epiphany.model.inventory.Wearable.WearableType;

public class Inventory {
	private static final int ROW = 4;
	private static final int COL = 8;
	Item[][] items = new Item[ROW][COL];
	StatisticsHandler statsHandler;
	
	Consumable consumableBeingUsed = null;
	Wearable helm = null;
	Wearable armor = null;
	Wearable leftHand = null;
	Wearable rightHand = null;
	Wearable gloves = null;
	Wearable boots = null;

	public Inventory(StatisticsHandler statsHandler) {
		this.statsHandler = statsHandler;
		clear();
		
		helm = new Wearable(WearableType.HELM).setMainAttributeBonus(5, 5, 5, 5, 5).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		
		// Testing
		
		items[0][0] = new Consumable(ConsumableType.MP_POTION, 15, 3);
		items[3][0] = new Wearable(WearableType.HELM).setMainAttributeBonus(5, 5, 5, 5, 5).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][1] = new Wearable(WearableType.HELM).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][2] = new Wearable(WearableType.ARMOR).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][3] = new Wearable(WearableType.BOOTS).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][4] = new Wearable(WearableType.LEFT_HAND).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][5] = new Wearable(WearableType.RIGHT_HAND).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][6] = new Wearable(WearableType.GLOVES).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		items[3][7] = new Wearable(WearableType.BOOTS).setMainAttributeBonus(4, 4, 4, 4, 4).
				setSubAttributeBonus(5, 5, 5, 5, 5, 5, 5, 5);
		
	}

	public void update(float delta) {
		if (consumableBeingUsed != null) {
			if (!consumableBeingUsed.updateEffect(statsHandler, delta)) {
				consumableBeingUsed = null;
			}
		}
	}
	
	public void use(int row, int col) {
		if (items[row][col] == null)
			return;
		
		switch (items[row][col].getItemClass()) {
		case WEARABLE:
			wear(row, col);
			break;
		case CONSUMABLE:
			consume(row, col);
			break;
		}
	}
	
	private void wear(int row, int col) {
		Wearable wearable = (Wearable) items[row][col];

		switch (wearable.getWearableType()) {
		case HELM:
			helm = putToItemSlot(helm, row, col);
			
			break;
		case ARMOR:
			armor = putToItemSlot(armor, row, col);
			break;
		case LEFT_HAND:
			leftHand = putToItemSlot(leftHand, row, col);
			break;
		case RIGHT_HAND:
			rightHand = putToItemSlot(rightHand, row, col);
			break;
		case GLOVES:
			gloves = putToItemSlot(gloves, row, col);
			break;
		case BOOTS:
			boots = putToItemSlot(boots, row, col);
			break;
		}
	}
	
	/**
	 * A generic method. Puts the currently equipped item to the item slot and tries to replace the currently equipped item.
	 * If successful, it returns the clicked item then places the currently equipped item to item slots, 
	 * else it will return the current equipped item (No change).
	 * @param equippedItem
	 * @param row
	 * @param col
	 * @return the item to be equipped
	 */
	private Wearable putToItemSlot(Wearable equippedItem, int row, int col) {
		Wearable item = (Wearable) items[row][col];
		if (equippedItem != null) {
			if (putInInventory(equippedItem)) {
				equippedItem.remove(statsHandler);
				items[row][col] = null;
				item.use(statsHandler);
				return item;
			}
			
			//...
			System.out.println("No more space in item slots");
			return equippedItem;
		} else {
			item.use(statsHandler);
			items[row][col] = null;
			return item;
		}
		
	}
	
	private void consume(int row, int col) {
		Consumable consumable = (Consumable) items[row][col];
		consumable.use(statsHandler);
		
		if (consumableBeingUsed != null)
			consumableBeingUsed.addRegenPoints((int) consumable.getRegenPoints());
		consumableBeingUsed = consumable;
		
		if (consumable.isEmpty()) {
			items[row][col] = null;
		}
	}
	
	public void singleClicked(int row, int col) {
		Item item = items[row][col];
		if (item == null)
			return;
		
		switch (item.getItemClass()) {
		case WEARABLE:
			
			break;
		case CONSUMABLE:
			break;
		}
	}
	
	public boolean putInInventory(Item item) {
		for (int row = items.length - 1; row >= 0; --row) {
			for (int col = 0; col < items[row].length; ++col) {
				if (items[row][col] == null) {
					items[row][col] = item;
					return true;
				}
			}
		}
		return false;
	}
	
	
	private void clear() {
		for (int row = 0; row < ROW; ++row) {
			for (int col = 0; col < COL; ++col) {
				items[row][col] = null;
			}
		}
	}
	
	public void removeEquippedItem(Wearable wearable) {
		if (!putInInventory(wearable)) {
			//....
			System.out.println("No more space in item slots");
		}

		switch (wearable.getWearableType()) {
		case HELM:
			helm.remove(statsHandler);
			helm = null;
			break;
		case ARMOR:
			armor.remove(statsHandler);
			armor = null;
			break;
		case LEFT_HAND:
			leftHand.remove(statsHandler);
			leftHand = null;
			break;
		case RIGHT_HAND:
			rightHand.remove(statsHandler);
			rightHand = null;
			break;
		case GLOVES:
			gloves.remove(statsHandler);
			gloves = null;
			break;
		case BOOTS:
			boots.remove(statsHandler);
			boots = null;
			break;
		}
	}
	
	public Wearable getHelm() { return helm; }
	public Wearable getArmor() { return armor; }
	public Wearable getLeftHand() { return leftHand; }
	public Wearable getRightHand() { return rightHand; }
	public Wearable getGloves() { return gloves; }
	public Wearable getBoots() { return boots; }
	
	public final Item[][] getItems() { return items; }
}
