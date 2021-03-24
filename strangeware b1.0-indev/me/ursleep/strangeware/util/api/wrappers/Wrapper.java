package me.ursleep.strangeware.util.api.wrappers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;

public class Wrapper {

	public static volatile Wrapper INSTANCE = new Wrapper();
	public static Minecraft mc = Minecraft.getMinecraft();
	
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().player;
    }

    public WorldClient world() {
        return Wrapper.INSTANCE.mc().world;
    }

    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }

    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRenderer;
    }
    
    public void sendPacket(@SuppressWarnings("rawtypes") Packet packet) {
        this.player().connection.sendPacket(packet);
    }
    
    public InventoryPlayer inventory() { 
		return this.player().inventory; 
	}
	
	public PlayerControllerMP controller() { 
		return Wrapper.INSTANCE.mc().playerController; 
	}

}
