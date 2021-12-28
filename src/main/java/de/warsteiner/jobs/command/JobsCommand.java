package de.warsteiner.jobs.command;
 
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs; 
import de.warsteiner.jobs.utils.GuiManager;

public class JobsCommand  implements CommandExecutor {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		 
		int length = args.length;
		
		if(sender instanceof Player) {
		
			GuiManager gui = plugin.getGUIManager();
			Player player = (Player) sender;
			 
			if (length == 0) {
			 
				gui.createMainGUIOfJobs(player);
			} else {
				plugin.getCommand().execute(player, args);
			}
		} else {
			plugin.getLogger().warning("�cThis Command is only for Players. Please use /Jobsadmin.");
		}
		
		return false;
	}
}