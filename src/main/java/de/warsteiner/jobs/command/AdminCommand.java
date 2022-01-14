package de.warsteiner.jobs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class AdminCommand implements CommandExecutor {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public static String prefix = "§9UltimateJobs §8-> §7";

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		int length = args.length;

		if (plugin.getAPI().checkPermissions(sender, "admin.command")) {

			if (length == 0) {
				sendHelp(sender);
			} else {
				String ar = args[0].toLowerCase();

				if (find(ar) == null) {
					sender.sendMessage(prefix + "§7Error! §7Use §6/Jobsadmin help§7.");
					return true;
				} else {

					AdminSubCommand cmd = find(ar);

					cmd.perform(sender, args);

				}

			}

		} else {
			sender.sendMessage(prefix + "§7Error! §7You dont have Permissions.");
		}
		return false;
	}

	public AdminSubCommand find(String given) {
		for (AdminSubCommand subCommand : plugin.getAdminSubCommandManager().getSubCommandList()) {
			if (given.equalsIgnoreCase(subCommand.getName().toLowerCase())) {
				return subCommand;
			}
		}
		return null;
	}

	public static void sendHelp(CommandSender sender) {
		sender.sendMessage("§7");
		sender.sendMessage(" §8| §9UltimateJobs §8- §4Admin Help §8|");
		sender.sendMessage("§8-> §6/JobsAdmin setmax <name> <value>");
		sender.sendMessage("§8-> §6/JobsAdmin reload <type>");
		sender.sendMessage("§8-> §6/JobsAdmin help");
		sender.sendMessage("§8-> §6/JobsAdmin addons");
		sender.sendMessage("§8-> §6/JobsAdmin version");
		sender.sendMessage("§7");
	}

}
