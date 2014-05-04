package com.nickan.epiphany.model.inventory;

import com.nickan.epiphany.model.StatisticsHandler;

public class Consumable extends Item {
	private int quantity;
	private float regenPerSecond;
	private float regenPoints;
	public enum ConsumableType { HP_POTION, MP_POTION };
	private final ConsumableType consumableType;

	public Consumable(ConsumableType consumableType, float regenPoints, float regenPerSecond) {
		super(ItemClass.CONSUMABLE);
		this.consumableType = consumableType;
		this.quantity = 1;
		this.regenPoints = regenPoints;
		this.regenPerSecond = regenPerSecond;
	}

	public boolean updateEffect(StatisticsHandler statsHandler, float delta) {
		switch (consumableType) {
		case HP_POTION:
			return updateHpEffect(statsHandler, delta);
		case MP_POTION:
			return updateMpEffect(statsHandler, delta);
		}
		return false;
	}

	private boolean updateHpEffect(StatisticsHandler statsHandler, float delta) {
		// Does not need to be healed
		if (statsHandler.isCurrentHpFull())
			return false;

		float regen = delta * regenPerSecond;
		// If there is enough regenPoints to be added to char hp by heal per second
		if (regenPoints >= regen) {
			regenPoints -= regen;
			statsHandler.incCurrentHp(regen);
			return true;
		}
		statsHandler.roundCurrentHpToInt();
		return false;
	}
	
	private boolean updateMpEffect(StatisticsHandler statsHandler, float delta) {
		// Does not need to be healed
		if (statsHandler.isCurrentMpFull())
			return false;

		float regen = delta * regenPerSecond;
		// If there is enough regenPoints to be added to char hp by heal per second
		if (regenPoints >= regen) {
			regenPoints -= regen;
			statsHandler.incCurrentMp(regen);
			return true;
		}
		statsHandler.roundCurrenMpToInt();
		return false;
	}

	@Override
	public void use(StatisticsHandler statsHandler) {
		addQuantity(-1);
	}

	public boolean isEmpty() { return (quantity < 1) ? true : false; }

	public void addQuantity(int add) { quantity += add; }

	public void addRegenPoints(int addPoints) { this.regenPoints += addPoints; }
	
	public float getRegenPoints() { return regenPoints; }
	
	public float getRegenPerSecond() { return regenPerSecond; }
	
	public final ConsumableType getConsumableType() { return consumableType; }

}
