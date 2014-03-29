package com.nickan.epiphany.model.inventory;

import com.nickan.epiphany.model.StatisticsHandler;

public class Wearable extends Item {
	public enum WearableType { HELM, ARMOR, LEFT_HAND, RIGHT_HAND, GLOVES, BOOTS };
	private final WearableType wearableType;
	private int addStr;
	private int addDex;
	private int addVit;
	private int addAgi;
	private int addWis;
	
	private int addAtkDmg;
	private int addHit;
	private float addCrt;
	private int addAvd;
	private float addAtkSpd;
	private int addDef;
	
	private int addHp;
	private int addMp;
	
	public Wearable(WearableType wearableType) {
		super(ItemClass.WEARABLE);
		this.wearableType = wearableType;
	}
	
	/**
	 * 
	 * @param addStr
	 * @param addDex
	 * @param addVit
	 * @param addAgi
	 * @param addWis
	 * @return this for chaining
	 */
	public Wearable setMainAttributeBonus(int addStr, int addDex, int addVit, int addAgi, int addWis) {
		this.addStr = addStr;
		this.addDex = addDex;
		this.addVit = addVit;
		this.addAgi = addAgi;
		this.addWis = addWis;
		return this;
	}
	
	/**
	 * 
	 * @param addAtkDmg
	 * @param addHit
	 * @param addCrt
	 * @param addDef
	 * @param addHp
	 * @param addAtkSpd
	 * @param addAvd
	 * @param addMp
	 * @return this for chaining
	 */
	public Wearable setSubAttributeBonus(int addAtkDmg, int addHit, float addCrt, int addDef, int addHp, 
			float addAtkSpd, int addAvd, int addMp) {
		this.addAtkDmg = addAtkDmg;
		this.addHit = addHit;
		this.addCrt = addCrt;
		this.addDef = addDef;
		this.addHp = addHp;
		this.addAtkSpd = addAtkSpd;
		this.addAvd = addAvd;
		this.addMp = addMp;
		return this;
	}
	
	@Override
	public void use(StatisticsHandler statsHandler) {
		statsHandler.addAddedStr(addStr);
		statsHandler.addAddedDex(addDex);
		statsHandler.addAddedVit(addVit);
		statsHandler.addAddedAgi(addAgi);
		statsHandler.addAddedWis(addWis);
		statsHandler.addAddedAtkDmg(addAtkDmg);
		statsHandler.addAddedHit(addHit);
		statsHandler.addAddedCrt(addCrt);
		statsHandler.addAddedDef(addDef);
		statsHandler.addAddedHp(addHp);
		statsHandler.addAddedAtkSpd(addAtkSpd);
		statsHandler.addAddedAvd(addAvd);
		statsHandler.addAddedMp(addMp);
	}
	
	public void remove(StatisticsHandler statsHandler) {
		statsHandler.addAddedStr(-addStr);
		statsHandler.addAddedDex(-addDex);
		statsHandler.addAddedVit(-addVit);
		statsHandler.addAddedAgi(-addAgi);
		statsHandler.addAddedWis(-addWis);
		statsHandler.addAddedAtkDmg(-addAtkDmg);
		statsHandler.addAddedHit(-addHit);
		statsHandler.addAddedCrt(-addCrt);
		statsHandler.addAddedDef(-addDef);
		statsHandler.addAddedHp(-addHp);
		statsHandler.addAddedAtkSpd(-addAtkSpd);
		statsHandler.addAddedAvd(-addAvd);
		statsHandler.addAddedMp(-addMp);
	}
	
	public final WearableType getWearableType() {
		return wearableType;
	}
}
