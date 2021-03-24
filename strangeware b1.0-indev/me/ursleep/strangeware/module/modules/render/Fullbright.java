package me.ursleep.strangeware.module.modules.render;

import org.lwjgl.input.Keyboard;

import me.ursleep.strangeware.module.Category;
import me.ursleep.strangeware.module.Module;

public class Fullbright extends Module {
	
	public Fullbright() {
		super("Fullbright", "Adding gamma to you", Category.RENDER);
		this.setKey(Keyboard.KEY_NONE);
	}
	
	@Override
	public void onEnable() {
			mc.gameSettings.gammaSetting = 1000f;
	}
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1f;
	}
}
	
