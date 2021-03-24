package me.ursleep.strangeware.module;

// Yo, skii, if ure reading this, don't touch this, if u wanna add new category just dm me (I just wanna know, if you add a new category)

public enum Category {
	COMBAT("Combat"), CHAT("Chat"), RENDER("Render"), PLAYER("Player"), MISC("Miscellaneous"), CLIENT("Client"), MOVEMENT("Movement"), HUD("Hud");
	
	public String name;
	public int moduleIndex;
	
	Category(String name) {
		this.name = name;
	}

}
