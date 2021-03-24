package me.ursleep.strangeware.module.modules.client;

import me.ursleep.strangeware.module.Module;

import org.lwjgl.input.Keyboard;

import me.ursleep.strangeware.Main;
import me.ursleep.strangeware.module.Category;

public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", "GUI for toggling/binding/setting modules.", Category.CLIENT);
		this.setKey(Keyboard.KEY_U);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(Main.instance.clickGui);
		this.setToggled(false);
	}
}
