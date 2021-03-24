package me.ursleep.strangeware.module;

import java.util.ArrayList;

import java.util.List;

import me.ursleep.strangeware.Main;
import me.ursleep.strangeware.module.modules.client.ClickGUI;
import me.ursleep.strangeware.module.modules.client.HUD;
import me.ursleep.strangeware.module.modules.combat.CrystalAura;
import me.ursleep.strangeware.module.modules.misc.FakePlayer;
import me.ursleep.strangeware.module.modules.movement.Sprint;
import me.ursleep.strangeware.module.modules.render.Fullbright;

public class ModuleManager {

	// initialization of our modules. yea
	public ArrayList<Module> modules;
	

	public ModuleManager() {
		(modules = new ArrayList<Module>()).clear();
		// Movement modules
		this.modules.add(new Sprint());
		// Combat Modules
		this.modules.add(new CrystalAura());
		// Render Modules
		this.modules.add(new Fullbright());
		// Miscellaneous Modules
		this.modules.add(new FakePlayer());
		// Client Modules
		this.modules.add(new ClickGUI());
		this.modules.add(new HUD());
	}
	
	public Module getModule (String name) {
		for(Module m : this.modules) {
			if(m.getName().equalsIgnoreCase(name)) {
				return m;
			}

		}
		return null;
	}
	
	public ArrayList<Module> getModuleList() {
		return this.modules;
	}
	
	public static List<Module> getModulesByCategory(Category c) {
		List<Module> modules = new ArrayList<Module>();
		
		for(Module m : Main.instance.moduleManager.modules)
			if(m.getCategory() == c)
				modules.add(m);
		
		return modules;
	}
	
	public ArrayList<Module> getModulesInCategory(Category c) {
		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module m : this.modules) {
			if (m.getCategory() == c) {
				mods.add(m);
			}
		}
		return modules;
	}
}
