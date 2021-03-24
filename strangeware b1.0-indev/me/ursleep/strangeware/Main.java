package me.ursleep.strangeware;

import org.lwjgl.input.Keyboard;

import me.ursleep.strangeware.command.CommandManager;
import me.ursleep.strangeware.module.Module;
import me.ursleep.strangeware.module.ModuleManager;
import me.ursleep.strangeware.module.modules.client.HUD;
import me.ursleep.strangeware.ui.gui.ClickGui;
import me.ursleep.strangeware.util.Refrence;
import me.ursleep.strangeware.util.api.ConfigSaveHook;
import me.ursleep.strangeware.util.settings.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

@Mod(modid = Refrence.MOD_ID, name = Refrence.NAME, version = Refrence.VERSION)
public class Main {

	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		Main.instance = new Main();
		Main.instance.initinstance();

	}
	
	public static Main instance;
	public SettingsManager settingsManager;
	public ModuleManager moduleManager;
	public ConfigSaveHook configSaveHook;
	public CommandManager cmdManager;
	public ClickGui clickGui;
	
	public void initinstance() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new HUD());
		settingsManager = new SettingsManager();
		moduleManager = new ModuleManager();
		configSaveHook = new ConfigSaveHook();
		cmdManager = new CommandManager();
		clickGui = new ClickGui();
	}

	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void PostInit(FMLPreInitializationEvent event) {

	}

	@SubscribeEvent
	public void key(KeyInputEvent e) {
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
			return;
		try {
			if (Keyboard.isCreated()) {
				if (Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode <= 0)
						return;
					for (Module m : moduleManager.modules) {
						if (m.getKey() == keyCode && keyCode > 0) {
							m.toggle();
						}
					}
				}
			}
		} catch (Exception q) {
			q.printStackTrace();
		}
	}
}