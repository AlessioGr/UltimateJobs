package de.warsteiner.jobs.api;

import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.Action;

public class Job {

	private String idt;
	private YamlConfiguration cf;
	private String display;
	private Action action;
	private String icon;
	private int slot;
	private double price;
	private String perm;
	private List<String> stats_message;
	private List<String> worlds;
	private List<String> lore;
	private List<String> idslist;

	public Job(String id, YamlConfiguration cfg) {
		idt = id;
		cf = cfg;
		display = cfg.getString("Display");
		perm = cfg.getString("Permission");
		action = Action.valueOf(cfg.getString("Action"));
		icon = cfg.getString("Material");
		slot = cfg.getInt("Slot");
		price = cfg.getDouble("Price");
		stats_message = cfg.getStringList("Stats");
		worlds = cfg.getStringList("Worlds");
		lore = cfg.getStringList("Lore");
		idslist = cfg.getStringList("IDS.List");

	}

	public BarColor getBarColor() {
		return BarColor.valueOf(cf.getString("ColorOfBossBar"));
	}

	public String getLevelDisplay(int i) {
		return cf.getString("LEVELS." + i + ".Display");
	}

	public List<String> getLevelCommands(int i) {
		return cf.getStringList("LEVELS." + i + ".Command");
	}

	public List<String> getPermissionsLore() { 
		return cf.getStringList("PermLore");
	}

	public String getPermMessage() { 
		return cf.getString("PermMessage");
	}

	public boolean hasPermission() {
		return cf.getString("Permission") != null;
	}

	public String getPermission() {
		return perm;
	}

	public Job get() {
		return this;
	}
	
	public String getAlonsoLevelsOnLevel(int level) {
		return cf.getString("LEVELS." + level + ".AlonsoLevelsExp");
	}

	public boolean isAlonsoLevelsOnLevel(int level) {
		return cf.getString("LEVELS." + level + ".AlonsoLevelsExp") != null;
	}
	
	public String getAlonsoLevels(String id) {
		return cf.getString("IDS." + id + ".AlonsoLevelsExp");
	}

	public boolean isAlonsoLevels(String id) {
		return cf.getString("IDS." + id + ".AlonsoLevelsExp") != null;
	}

	public String getNameOfLevel(int level) {
		return cf.getString("LEVELS." + level + ".Display");
	}

	public double getExpOfLevel(int level) {
		return cf.getDouble("LEVELS." + level + ".Need");
	}

	public String getDisplayOf(String id) {
		return cf.getString("IDS." + id + ".Display");
	}

	public int gettChanceOf(String id) {
		return cf.getInt("IDS." + id + ".Chance");
	}

	public double getRewardOf(String id) {
		return cf.getDouble("IDS." + id + ".Money");
	}

	public List<String> getCustomitems() {
		return cf.getStringList("Items");
	}

	public double getExpOf(String id) {
		return cf.getDouble("IDS." + id + ".Exp");
	}

	public double getPointsOf(String id) {
		return cf.getDouble("IDS." + id + ".Points");
	}

	public List<String> getIDList() {
		return idslist;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public List<String> getStatsMessage() {
		return stats_message;
	}

	public double getPrice() {
		return price;
	}

	public int getSlot() {
		return slot;
	}

	public String getIcon() {
		return icon;
	}

	public Action getAction() {
		return action;
	}

	public String getDisplay() {
		return UltimateJobs.getPlugin().getAPI().toHex(display).replaceAll("&", "§");
	}

	public String getID() {
		return idt;
	}

}
