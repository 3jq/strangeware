package me.ursleep.strangeware.module;

import me.ursleep.strangeware.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Module {
	
	protected static Minecraft mc = Minecraft.getMinecraft();
	
	public String name, description;
	private int key;
	private Category category;
	public boolean toggled;
	public boolean visible = true;
	
	public Module(String name, String description, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.key = 0;
		this.category = category;
		this.toggled = false;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getKey() {
		return key;
	}
	
	public void setKey(int key) {
		this.key = key;
		if (Main.instance.configSaveHook != null) {
			Main.instance.configSaveHook.save();
		}
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		
		if(this.toggled) {
			this.onEnable();
		}else {
			this.onDisable();
		}
		
		if (Main.instance.configSaveHook != null) {
			Main.instance.configSaveHook.save();
		}
	}
	public void toggle() {
		this.toggled = !this.toggled;
		
		if(this.toggled) {
			this.onEnable();
		}else {
			this.onDisable();
		}
		if (Main.instance.configSaveHook != null) {
			Main.instance.configSaveHook.save();
		}
	}
	
	public void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	public String getName() {
		return this.name;
	}
	public Category getCategory() {
		return this.category;
	}

	public void onUpdate() {
		
	}

	public boolean onSendChatMessage(String s) {
		return false;
	}

	public boolean onRecieveChatMessage(SPacketChat packet) {
		return false;
	}

	public void onClientTick(ClientTickEvent event) {
		
	}
}
