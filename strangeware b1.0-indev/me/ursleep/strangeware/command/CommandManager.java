package me.ursleep.strangeware.command;

import java.util.concurrent.CopyOnWriteArrayList;

import me.ursleep.strangeware.Main;

public class CommandManager {

	
	public CopyOnWriteArrayList<Command> commands = new CopyOnWriteArrayList<>();
	
	public CommandManager() {
		
	}
	
	public CopyOnWriteArrayList<Command> getCommands() {
		return commands;
	}
	
	public static Command getCommandByName(String commandName) {
		for(Command cmd : Main.instance.cmdManager.getCommands()) {
			if((cmd.getName().trim().equalsIgnoreCase(commandName))
			|| (cmd.toString().trim().equalsIgnoreCase(commandName
					.trim()))) {
				return cmd;
			}
		}
		return null;
	}
}
