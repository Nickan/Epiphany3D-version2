package com.nickan.epiphany.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.epiphany.model.Character;
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
	
	public HeadsUpDisplay() {
		atlas = new TextureAtlas("gamescreen/hudtexture.pack");
		skin = new Skin(atlas);
	}
	
	public void drawPauseBackground(SpriteBatch batch) {
		batch.begin();
		batch.draw(skin.getRegion("inventorybackground"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
	
	public void drawPauseHud(SpriteBatch batch, Character player) {
		batch.begin();
		batch.draw(skin.getRegion("statsbox"), strPos.x, strPos.y, unitX * 1.5f, unitY * 1.5f);
		batch.draw(skin.getRegion("statsbox"), dexPos.x, dexPos.y, unitX * 1.5f, unitY * 1.5f);
		batch.draw(skin.getRegion("statsbox"), vitPos.x, vitPos.y, unitX * 1.5f, unitY * 1.5f);
		batch.draw(skin.getRegion("statsbox"), agiPos.x, agiPos.y, unitX * 1.5f, unitY * 1.5f);
		batch.draw(skin.getRegion("statsbox"), wisPos.x, wisPos.y, unitX * 1.5f, unitY * 1.5f);
		
		drawInventory(batch, player.inventory);
		batch.end();
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

				if (item == null) {
					continue;
				}

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
		
	}
	
	
	public void show() {
		
	}
	
	public void resize(int width, int height) {
		unitX = width / 24f;
		unitY = height / 18f;
		
		strPos.set(unitX * 1.5f, unitY * 13f);
		dexPos.set(unitX * 1.5f, unitY * 11.5f);
		vitPos.set(unitX * 1.5f, unitY * 10.0f);
		agiPos.set(unitX * 1.5f, unitY * 8.5f);
		wisPos.set(unitX * 1.5f, unitY * 7.0f);
	}
	
	public void dispose() {
		skin.dispose();
	}
}
