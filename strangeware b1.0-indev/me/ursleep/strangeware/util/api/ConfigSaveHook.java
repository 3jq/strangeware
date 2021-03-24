package me.ursleep.strangeware.util.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import me.ursleep.strangeware.Main;
import me.ursleep.strangeware.module.Module;
import me.ursleep.strangeware.util.settings.Setting;
import net.minecraft.client.Minecraft;

public class ConfigSaveHook {

	private File dir;
	private File dataFile;

	public ConfigSaveHook() {
		dir = new File(Minecraft.getMinecraft().mcDataDir, "strangeware");
		if (!dir.exists()) {
			dir.mkdir();
		}
		dataFile = new File(dir, "StrangewareSettings.config");
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		this.load();
	}

	public void save() {
		ArrayList<String> toSave = new ArrayList<String>();

		for (Module mod : Main.instance.moduleManager.modules) {
			toSave.add("MOD:" + mod.getName() + ":" + mod.isToggled() + ":" + mod.getKey());
		}

		for (Setting set : Main.instance.settingsManager.getSettings()) {
			if (set.isCheck()) {
				toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValDouble());
			}

			if (set.isCombo()) {
				toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValString());
			}

			if (set.isSlider()) {
				toSave.add("SET:" + set.getName() + ":" + set.getParentMod().getName() + ":" + set.getValDouble());
			}
		}

		try {
			PrintWriter pw = new PrintWriter(this.dataFile);
			for (String str : toSave) {
				pw.println(str);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		ArrayList<String> lines = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String s : lines) {
			String[] args = s.split(":");
			if (args[0].equals("MOD")) {
				Module m = Main.instance.moduleManager.getModule(args[1]);
				if (m != null) {
					m.setToggled(Boolean.parseBoolean(args[2]));
					m.setKey(Integer.parseInt(args[3]));
				}
			}
		}
	}
}
