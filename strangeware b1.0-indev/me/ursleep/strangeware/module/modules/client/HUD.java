package me.ursleep.strangeware.module.modules.client;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.ursleep.strangeware.Main;
import me.ursleep.strangeware.module.Category;
import me.ursleep.strangeware.module.Module;
import me.ursleep.strangeware.util.Refrence;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD extends Module {

	public HUD() {
		super("HUD", "Draws some info on your screen", Category.CLIENT);
		this.setKey(Keyboard.KEY_NONE);
	}
	
	@SubscribeEvent
	public void onEnable(RenderGameOverlayEvent event) {
		
			ScaledResolution sr = new ScaledResolution(mc);
			FontRenderer fr = mc.fontRenderer;
			
			
			if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				final int[] counter = {1};
				fr.drawStringWithShadow("Strangeware" + " " + Refrence.VERSION, 2, 1, rainbow(counter[0] * 75));
				counter[0]++;
			}
			if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				int y = 2;
				final int[] counter = {1};
				for(Module mod : Main.instance.moduleManager.getModuleList()) {
					if(!mod.getName().equalsIgnoreCase("") && mod.isToggled()) {
						fr.drawStringWithShadow(mod.getName(),  sr.getScaledWidth() -fr.getStringWidth(mod.getName()) - 2, y, rainbow(counter[0] * 75));
						y += fr.FONT_HEIGHT;
						counter[0]++;
					}
				}
			}
			}
	
		public void onDisable() {
		}
	
	public static int rainbow(int delay) {
		double rainbowState = Math.ceil(System.currentTimeMillis() + delay / 20.0);
		rainbowState %= 9999;
		return Color.getHSBColor((float) (rainbowState / 9999.0f), 0.5f, 1f).getRGB();
	}
}
