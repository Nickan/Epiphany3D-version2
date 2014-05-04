package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.epiphany.model.Character;
import com.nickan.epiphany.model.StatisticsHandler;
import com.nickan.epiphany.model.inventory.Consumable;
import com.nickan.epiphany.model.inventory.Inventory;
import com.nickan.epiphany.model.inventory.Item;
import com.nickan.epiphany.model.inventory.Wearable;

public class HeadsUpDisplay {
	private Skin skin;
	private TextureAtlas atlas;
	AllGameButtonsView allButtons;
	
	private float unitX;
	private float unitY;
	
	private static final Vector2 strPos = new Vector2();
	private static final Vector2 dexPos = new Vector2();
	private static final Vector2 vitPos = new Vector2();
	private static final Vector2 agiPos = new Vector2();
	private static final Vector2 wisPos = new Vector2();
	
	public enum PauseWindow { SHOW_STATUS, SHOW_ITEMS };
	public enum SubScreenMode { SHOW_ITEM_DESC, NONE };
	
	private PauseWindow pauseWindow = PauseWindow.SHOW_STATUS;
	private SubScreenMode subScreen1 = SubScreenMode.NONE;
	private SubScreenMode subScreen2 = SubScreenMode.NONE;
	
	// FIXME Scaling fonts is not a very good idea, most likely I will need a multiple BitmapFonts
	private BitmapFont arial;
	
	Item itemToShowDesc = null;
	
	public HeadsUpDisplay() {
		atlas = new TextureAtlas("gamescreen/hudtexture.pack");
		skin = new Skin(atlas);
		arial = new BitmapFont(Gdx.files.internal("gamescreen/arial.fnt"));
	}
	
	public void drawPauseBackground(SpriteBatch batch) {
		batch.begin();
		float widthUnit = unitX * 0.8f;
		float heightUnit = unitY * 0.8f;
		int totalHeightUnit = 16;
		int totalWidthUnit = 14;
		
		// Left border corners
		batch.draw(skin.getRegion("topleft"), 0, heightUnit * totalHeightUnit, widthUnit, heightUnit);
		batch.draw(skin.getRegion("bottomleft"), 0, 0, widthUnit, heightUnit);
		batch.draw(skin.getRegion("bottomright"), widthUnit * totalWidthUnit, 0, widthUnit, heightUnit);
		batch.draw(skin.getRegion("topright"), widthUnit * totalWidthUnit, heightUnit * totalHeightUnit, 
				widthUnit, heightUnit);
		
		// Right border corners
		float rightUnitWidthAdj = widthUnit * (totalWidthUnit + 1);
		batch.draw(skin.getRegion("topleft"), 0 + rightUnitWidthAdj, heightUnit * totalHeightUnit, widthUnit, heightUnit);
		batch.draw(skin.getRegion("bottomleft"), 0 + rightUnitWidthAdj, 0, widthUnit, heightUnit);
		batch.draw(skin.getRegion("bottomright"), (widthUnit * totalWidthUnit) + rightUnitWidthAdj, 0, 
				widthUnit, heightUnit);
		batch.draw(skin.getRegion("topright"), (widthUnit * totalWidthUnit) + rightUnitWidthAdj, 
				heightUnit * totalHeightUnit, widthUnit, heightUnit);
		
		float cutScalePrevention = 0.1f;
		float cutPreventionY = heightUnit * cutScalePrevention;
		for (int height = totalHeightUnit - 1; height > 0; --height) {
			// Left border
			// Left grid
			batch.draw(skin.getRegion("left"), 0, heightUnit * height, widthUnit, heightUnit + cutPreventionY);
			// Right grid
			batch.draw(skin.getRegion("right"), widthUnit * totalWidthUnit, heightUnit * height, 
					widthUnit, heightUnit + cutPreventionY);
			
			// Right border
			// Left grid
			batch.draw(skin.getRegion("left"), 0 + rightUnitWidthAdj, heightUnit * height, 
					widthUnit, heightUnit + cutPreventionY);
			// Right grid
			batch.draw(skin.getRegion("right"), (widthUnit * totalWidthUnit) + rightUnitWidthAdj, heightUnit * height, 
					widthUnit, heightUnit + cutPreventionY);
		}
		
		float cutPreventionX = widthUnit * cutScalePrevention;
		for (int width = totalWidthUnit - 1; width > 0; --width) {
			// Left border
			// Bottom grid
			batch.draw(skin.getRegion("bottom"), widthUnit * width, 0, widthUnit + cutPreventionX, heightUnit);
			// Top grid
			batch.draw(skin.getRegion("top"), widthUnit * width, heightUnit * totalHeightUnit, 
					widthUnit + cutPreventionX, heightUnit);
			
			// Right border
			batch.draw(skin.getRegion("bottom"), (widthUnit * width) + rightUnitWidthAdj, 0, 
					widthUnit + cutPreventionX, heightUnit);
			// Top grid
			batch.draw(skin.getRegion("top"), (widthUnit * width) + rightUnitWidthAdj, heightUnit * totalHeightUnit, 
					widthUnit + cutPreventionX, heightUnit);
		}
		
		batch.end();
	}
	
	public void drawPauseHud(SpriteBatch batch, Character player) {
		batch.begin();
		drawPauseWindowButtonDesc(batch);
		drawSubScreen1(batch, player);
		drawSubScreen2(batch, player);
		batch.end();
	}
	
	private void drawPauseWindowButtonDesc(SpriteBatch batch) {
		arial.setColor(Color.YELLOW);
		arial.draw(batch, "Status", unitX * 2.85f, unitY * 16.2f);
		arial.draw(batch, "Inventory", unitX * 5.5f, unitY * 16.2f);
		arial.setColor(Color.WHITE);
	}
	
	private void drawSubScreen1(SpriteBatch batch, Character player) {
		switch (pauseWindow) {
		case SHOW_STATUS:
			StatisticsHandler handler = player.statsHandler;
			arial.draw(batch, "Knight", unitX * 2.0f, unitY * 12.5f);
			arial.draw(batch, "Lv. " + handler.level, unitX * 6.0f, unitY * 12.5f);
			
			arial.draw(batch, "Nickan", unitX * 2.0f, unitY * 11.5f);
			arial.draw(batch, "HP: " + (int) handler.getCurrentHp() + " / " + (int) handler.getFullHp(), 
					unitX * 2.0f, unitY * 10.5f);
			arial.draw(batch, "MP: " + (int) handler.getCurrentMp() + " / " + (int) handler.getFullMp(), 
					unitX * 2.0f, unitY * 8.5f);
			
			// Black bar background
			batch.setColor(Color.BLACK);
			batch.draw(skin.getRegion("whitetexture"), unitX * 2.0f, unitY * 9.0f, 
					unitX * 4f ,unitY * 0.5f);
			
			// Current life bar
			batch.setColor(Color.YELLOW);
			float currentLifeFraction = handler.getCurrentHp() / handler.getFullHp();
			batch.draw(skin.getRegion("whitetexture"), unitX * 2.0f, unitY * 9.0f, 
					(unitX * 4f) * currentLifeFraction, unitY * 0.5f);
			
			// Black bar background
			batch.setColor(Color.BLACK);
			batch.draw(skin.getRegion("whitetexture"), unitX * 2.0f, 
					unitY * 7.0f, unitX * 4f, unitY * 0.5f);
			
			// Current mana bar
			batch.setColor(Color.BLUE);
			float currentManaFraction = handler.getCurrentMp() / handler.getFullMp();
			batch.draw(skin.getRegion("whitetexture"), unitX * 2.0f, unitY * 7.0f, 
					(unitX * 4f) * currentManaFraction, unitY * 0.5f);
			
			batch.setColor(Color.WHITE);
			
			break;
		case SHOW_ITEMS:
			
			switch (subScreen1) {
			case SHOW_ITEM_DESC:
				showItemDesc(batch, unitX * 2.0f, unitY * 12.5f);
				
				// Show the name of item options
				arial.setColor(Color.YELLOW);
				arial.draw(batch, "Use", unitX * 2.4f, unitY * 2.2f);
				arial.draw(batch, "Cancel", unitX * 5f, unitY * 2.2f);
				arial.draw(batch, "Discard", unitX * 8f, unitY * 2.2f);
				arial.setColor(Color.WHITE);
				break;
			case NONE:
				
				break;
			}
			
			break;
		}
	}
	
	private void drawSubScreen2(SpriteBatch batch, Character player) {
		switch (pauseWindow) {
		case SHOW_STATUS:
			
			batch.draw(skin.getRegion("statsbox"), strPos.x, strPos.y, unitX * 1.5f, unitY * 1.5f);
			batch.draw(skin.getRegion("statsbox"), dexPos.x, dexPos.y, unitX * 1.5f, unitY * 1.5f);
			batch.draw(skin.getRegion("statsbox"), vitPos.x, vitPos.y, unitX * 1.5f, unitY * 1.5f);
			batch.draw(skin.getRegion("statsbox"), agiPos.x, agiPos.y, unitX * 1.5f, unitY * 1.5f);
			batch.draw(skin.getRegion("statsbox"), wisPos.x, wisPos.y, unitX * 1.5f, unitY * 1.5f);
			
			StatisticsHandler handler = player.statsHandler;
			arial.draw(batch, "" + (int) handler.getStr(), strPos.x + unitX * 0.3f, strPos.y + unitY);
			arial.draw(batch, "" + (int) handler.getDex(), dexPos.x + unitX * 0.3f, dexPos.y + unitY);
			arial.draw(batch, "" + (int) handler.getVit(), vitPos.x + unitX * 0.3f, vitPos.y + unitY);
			arial.draw(batch, "" + (int) handler.getAgi(), agiPos.x + unitX * 0.3f, agiPos.y + unitY);
			arial.draw(batch, "" + (int) handler.getWis(), wisPos.x + unitX * 0.3f, wisPos.y + unitY);
			
			
			arial.draw(batch, "STR", strPos.x + unitX * 1.5f, strPos.y + unitY);
			arial.draw(batch, "DEX", dexPos.x + unitX * 1.5f, dexPos.y + unitY);
			arial.draw(batch, "VIT", vitPos.x + unitX * 1.5f, vitPos.y + unitY);
			arial.draw(batch, "AGI", agiPos.x + unitX * 1.5f, agiPos.y + unitY);
			arial.draw(batch, "WIS", wisPos.x + unitX * 1.5f, wisPos.y + unitY);
			
			break;
		case SHOW_ITEMS:
			
			switch (subScreen2) {
			case SHOW_ITEM_DESC:
				break;
			case NONE:
				drawInventory(batch, player.inventory);
				break;
			}
			break;
		}
	}
	
	private void showItemDesc(SpriteBatch batch, float posX, float posY) {
		if (itemToShowDesc != null) {
			switch (itemToShowDesc.getItemClass()) {
			case CONSUMABLE:
				drawConsumableDesc(batch, posX, posY);
				break;
			case WEARABLE:
				drawWearableDesc(batch, posX, posY);
				break;
			}
		}
	}
	
	private void drawConsumableDesc(SpriteBatch batch, float posX, float posY) {
		Consumable consumable = (Consumable) itemToShowDesc;
		int regenTotalTime = (int) (consumable.getRegenPoints() / consumable.getRegenPerSecond());
		switch (consumable.getConsumableType()) {
		case HP_POTION:
			arial.draw(batch, "Heals health " + (int) consumable.getRegenPerSecond() + " for " + 
					regenTotalTime + " seconds", posX, posY);
			break;
		case MP_POTION:
			arial.draw(batch, "Recovers mana " + (int) consumable.getRegenPerSecond() + " for " + 
					regenTotalTime + " seconds", posX, posY);
			break;
		}
	}
	
	private void drawWearableDesc(SpriteBatch batch, float posX, float posY) {
		Wearable wearable = (Wearable) itemToShowDesc;
		
		arial.draw(batch, "" + wearable.getWearableType(), posX, posY);
		arial.draw(batch, "Req lv: ", posX, posY - (unitY * 0.7f));
		arial.draw(batch, "Dur: " + 100 + " / " + 100, posX, posY - (unitY * 1.4f));
		
		// Main attributes
		arial.draw(batch, wearable.getAddedStr() + "   STR", posX, posY - (unitY * 2.8f));
		arial.draw(batch, wearable.getAddedDex() + "   DEX ", posX, posY - (unitY * 3.5f));
		arial.draw(batch, wearable.getAddedVit() + "   VIT", posX, posY - (unitY * 4.2f));
		arial.draw(batch, wearable.getAddedAgi() + "   AGI", posX, posY - (unitY * 4.9f));
		arial.draw(batch, wearable.getAddedWis() + "   WIS", posX, posY - (unitY * 5.6f));
		
		// Sub attributes
		arial.draw(batch, wearable.getAddedAtkDmg() + "   atk dmg", posX, posY - (unitY * 7f));
		arial.draw(batch, wearable.getAddedHit() + "   hit", posX, posY - (unitY * 7.7f));
		arial.draw(batch, (int) wearable.getAddedCrit() + "   crt", posX, posY - (unitY * 8.4f));
		arial.draw(batch, wearable.getAddedAvd() + "   avd", posX, posY - (unitY * 9.1f));
		arial.draw(batch, (int) wearable.getAddedAtkSpd() + "   atk spd", posX + (unitX * 5f), posY - (unitY * 7f));
		arial.draw(batch, wearable.getAddedDef() + "   def", posX + (unitX * 5f), posY - (unitY * 7.7f));
		arial.draw(batch, wearable.getAddedHp() + "   hp", posX + (unitX * 5f), posY - (unitY * 8.4f));
		arial.draw(batch, wearable.getAddedMp() + "   mp", posX + (unitX * 5f), posY - (unitY * 9.1f));
		
	}
	
	
	private void drawInventory(SpriteBatch batch, Inventory inventory) {
		drawEquipment(batch, inventory);
		drawItems(batch, inventory.getItems());
	}
	
	private void drawItems(SpriteBatch batch, Item[][] items) {
		for (int col = 0; col < items.length; ++col) {
			for (int row = 0; row < items[col].length; ++row) {
				Item item = items[col][row];
				Button slot = allButtons.inventoryButtons[col][row];

				if (item == null) { continue; }

				switch (item.getItemClass()) {
				case CONSUMABLE:
					drawConsumable(batch, (Consumable) item, slot);
					break;
				case WEARABLE:
					drawWearable(batch, (Wearable) item, slot);
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void drawEquipment(SpriteBatch batch, Inventory inventory) {
		// The heck, if I would provide a new instance of bounds for the buttons, I will have
		// almost thirty new Rectangle instances, so just get the button.
		
		if (inventory.getHelm() != null) {
			drawWearable(batch, inventory.getHelm(), allButtons.head);
		}

		if (inventory.getArmor() != null) 
			drawWearable(batch, inventory.getArmor(), allButtons.body);

		if (inventory.getLeftHand() != null) 
			drawWearable(batch, inventory.getLeftHand(), allButtons.leftHand);

		if (inventory.getRightHand() != null)
			drawWearable(batch, inventory.getRightHand(), allButtons.rightHand);

		if (inventory.getGloves() != null)
			drawWearable(batch, inventory.getGloves(), allButtons.gloves);

		if (inventory.getBoots() != null)
			drawWearable(batch, inventory.getBoots(), allButtons.boots);
	}

	private void drawConsumable(SpriteBatch batch, Consumable consumable, Button slot) {
		switch (consumable.getConsumableType()) {
		case HP_POTION:
			drawItemSlotTexture(batch, skin.getRegion("hppotion"), slot);
			break;
		case MP_POTION:
			drawItemSlotTexture(batch, skin.getRegion("mppotion"), slot);
			break;
		default:
			break;
		}
	}

	private void drawWearable(SpriteBatch batch, Wearable wearable, Button slot) {
		switch (wearable.getWearableType()) {
		case HELM:
			drawItemSlotTexture(batch, skin.getRegion("helm"), slot);
			break;
		case ARMOR:
			drawItemSlotTexture(batch, skin.getRegion("breastplate"), slot);
			break;
		case LEFT_HAND:
			drawItemSlotTexture(batch, skin.getRegion("towershield"), slot);
			break;
		case RIGHT_HAND:
			drawItemSlotTexture(batch, skin.getRegion("sword"), slot);
			break;
		case GLOVES:
			drawItemSlotTexture(batch, skin.getRegion("gloves"), slot);
			break;
		case BOOTS:
			drawItemSlotTexture(batch, skin.getRegion("boots"), slot);
			break;
		}
	}
	
	private void drawItemSlotTexture(SpriteBatch batch, TextureRegion region, Button button) {
		float halfX = button.getWidth() / 2f;
		float halfY = button.getHeight() / 2f;
		
		float scaleX = 0.6f;
		float scaleY = 0.6f;
		float scaledX = button.getWidth() * scaleX;
		float scaledY = button.getHeight() * scaleY;
		batch.draw(region, (button.getX() + halfX) - (scaledX / 2), (button.getY() + halfY) - (scaledY / 2), 
				scaledX, scaledY);
	}
	
	
	public void drawGameHud(SpriteBatch batch) {
		float posX = 11.0f;
		float posY = 10f;
		batch.begin();
		
		batch.setColor(Color.YELLOW);
		batch.draw(skin.getRegion("whitetexture"), unitX * posX, unitY * posY, unitX * 2f, unitY * 0.2f);
		
		batch.setColor(Color.BLUE);
		batch.draw(skin.getRegion("whitetexture"), unitX * posX, unitY * (posY - 0.2f), unitX * 2f, unitY * 0.2f);
		
		batch.setColor(Color.WHITE);
		
		batch.end();
	}
	
	public void setPauseWindow(PauseWindow pauseWindow) { this.pauseWindow = pauseWindow; }
	public void setSubScreen1(SubScreenMode subScreenMode) { this.subScreen1 = subScreenMode; }
	public void setSubScreen2(SubScreenMode subScreenMode) { this.subScreen2 = subScreenMode; }
	
	
	public void show() {
		
	}
	
	public void resize(int width, int height) {
		unitX = width / 24f;
		unitY = height / 18f;
		
		float defaultFontScaleX = 0.05f;
		float defaultFontScaleY = 0.05f;
		arial.setScale(unitX * defaultFontScaleX, unitY * defaultFontScaleY);
		arial.setUseIntegerPositions(false);
		
		strPos.set(unitX * 13.5f, unitY * 10f);
		dexPos.set(unitX * 13.5f, unitY * 8.5f);
		vitPos.set(unitX * 13.5f, unitY * 7.0f);
		agiPos.set(unitX * 13.5f, unitY * 5.5f);
		wisPos.set(unitX * 13.5f, unitY * 4.0f);
	}
	
	public void dispose() {
		skin.dispose();
	}
}
