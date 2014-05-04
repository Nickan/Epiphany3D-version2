package com.nickan.epiphany.model.inventory;

import com.nickan.epiphany.model.StatisticsHandler;

public class Wearable extends Item {
	public enum WearableType { HELM, ARMOR, LEFT_HAND, RIGHT_HAND, GLOVES, BOOTS };
	private final WearableType wearableType;
	private int addedStr;
	private int addedDex;
	private int addedVit;
	private int addedAgi;
	private int addedWis;
	
	private int addedAtkDmg;
	private int addedHit;
	private float addedCrit;
	private int addedAvd;
	private float addedAtkSpd;
	private int addedDef;
	
	private int addedHp;
	private int addedMp;
	
	public Wearable(WearableType wearableType) {
		super(ItemClass.WEARABLE);
		this.wearableType = wearableType;
	}
	
	/**
	 * 
	 * @param addedStr
	 * @param addedDex
	 * @param addedVit
	 * @param addedAgi
	 * @param addedWis
	 * @return this for chaining
	 */
	public Wearable setMainAttributeBonus(int addedStr, int addedDex, int addedVit, int addedAgi, int addedWis) {
		this.addedStr = addedStr;
		this.addedDex = addedDex;
		this.addedVit = addedVit;
		this.addedAgi = addedAgi;
		this.addedWis = addedWis;
		return this;
	}
	
	/**
	 * 
	 * @param addedAtkDmg
	 * @param addedHit
	 * @param addedCrit
	 * @param addedDef
	 * @param addedHp
	 * @param addedAtkSpd
	 * @param addedAvd
	 * @param addedMp
	 * @return this for chaining
	 */
	public Wearable setSubAttributeBonus(int addedAtkDmg, int addedHit, float addedCrit, int addedDef, int addedHp, 
			float addedAtkSpd, int addedAvd, int addedMp) {
		this.addedAtkDmg = addedAtkDmg;
		this.addedHit = addedHit;
		this.addedCrit = addedCrit;
		this.addedDef = addedDef;
		this.addedHp = addedHp;
		this.addedAtkSpd = addedAtkSpd;
		this.addedAvd = addedAvd;
		this.addedMp = addedMp;
		return this;
	}
	
	@Override
	public void use(StatisticsHandler statsHandler) {
		statsHandler.addAddedStr(addedStr);
		statsHandler.addAddedDex(addedDex);
		statsHandler.addAddedVit(addedVit);
		statsHandler.addAddedAgi(addedAgi);
		statsHandler.addAddedWis(addedWis);
		statsHandler.addAddedAtkDmg(addedAtkDmg);
		statsHandler.addAddedHit(addedHit);
		statsHandler.addAddedCrt(addedCrit);
		statsHandler.addAddedDef(addedDef);
		statsHandler.addAddedHp(addedHp);
		statsHandler.addAddedAtkSpd(addedAtkSpd);
		statsHandler.addAddedAvd(addedAvd);
		statsHandler.addAddedHp(addedHp);
		statsHandler.addAddedMp(addedMp);
	}
	
	public void remove(StatisticsHandler statsHandler) {
		statsHandler.addAddedStr(-addedStr);
		statsHandler.addAddedDex(-addedDex);
		statsHandler.addAddedVit(-addedVit);
		statsHandler.addAddedAgi(-addedAgi);
		statsHandler.addAddedWis(-addedWis);
		statsHandler.addAddedAtkDmg(-addedAtkDmg);
		statsHandler.addAddedHit(-addedHit);
		statsHandler.addAddedCrt(-addedCrit);
		statsHandler.addAddedDef(-addedDef);
		statsHandler.addAddedHp(-addedHp);
		statsHandler.addAddedAtkSpd(-addedAtkSpd);
		statsHandler.addAddedAvd(-addedAvd);
		statsHandler.addAddedHp(-addedHp);
		statsHandler.addAddedMp(-addedMp);
	}
	
	public final WearableType getWearableType() { return wearableType; }

	public int getAddedStr() { return addedStr; }
	public int getAddedDex() { return addedDex; }
	public int getAddedVit() { return addedVit; }
	public int getAddedAgi() { return addedAgi; }
	public int getAddedWis() { return addedWis; }
	public int getAddedAtkDmg() { return addedAtkDmg; }
	public int getAddedHit() { return addedHit; }
	public float getAddedCrit() { return addedCrit; }
	public int getAddedAvd() { return addedAvd; }
	public float getAddedAtkSpd() { return addedAtkSpd; }
	public int getAddedDef() { return addedDef; }
	public int getAddedHp() { return addedHp; }
	public int getAddedMp() { return addedMp; }
	
}
