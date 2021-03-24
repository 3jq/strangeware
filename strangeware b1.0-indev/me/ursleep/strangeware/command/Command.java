package me.ursleep.strangeware.command;

import net.minecraft.client.Minecraft;

public class Command {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	public String name;
	public String description;
	public String usage;
	
	public Command(String name, String description, String usage) {
		this.name = name;
		this.description = description;
		this.usage = usage;
	}
	
	public void onExecute() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
