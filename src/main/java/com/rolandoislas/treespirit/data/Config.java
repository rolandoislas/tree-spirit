package com.rolandoislas.treespirit.data;

import com.rolandoislas.treespirit.data.spirit.EnumPlayerType;
import com.rolandoislas.treespirit.gui.renderer.EnumDeathWarning;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.ArrayList;

import static com.rolandoislas.treespirit.TreeSpirit.MODID;

/**
 * Created by Rolando on 2/21/2017.
 */
public class Config {
	private static final String BASE_LANG = MODID + ".config.";
	private static final String CATEGORY_DESTRUCTION = "destruction";
	private static final String CATEGORY_ADVANCED = "advanced";
	private static Configuration config;
	public static int deathTime;
	public static boolean randomTicksLog;
	public static EnumDeathWarning deathWarningType;
	public static boolean oneDimensionCorePerWorld;
	public static boolean neighborChangeLog;
	public static boolean zombiesDestroyCore;
	public static boolean skeletonsDestroyCore;
	public static boolean creepersDestroyCore;
	public static boolean onlyKillNormal;
	public static EnumPlayerType coreFeedsPlayerType;
	public static boolean worldTypeSkyTreeDefault;

	public static void setConfigFile(File configFile) {
		config = new Configuration(configFile);
	}

	public static void load() {
		config.load();
		// General
		config.setCategoryLanguageKey(Configuration.CATEGORY_GENERAL, BASE_LANG + "general");
		deathTime = config.getInt("deathtime", Configuration.CATEGORY_GENERAL, 10, 1, 60,
				"", BASE_LANG + "general.deathtime");
		randomTicksLog = config.getBoolean("randomticklog", Configuration.CATEGORY_GENERAL, true,
				"", BASE_LANG + "general.randomticklog");
		oneDimensionCorePerWorld = config.getBoolean("onedimcoreperworld", Configuration.CATEGORY_GENERAL,
				true, "", BASE_LANG + "general.onedimcoreperworld");
		neighborChangeLog = config.getBoolean("neighborchangelog", Configuration.CATEGORY_GENERAL,
				true, "", BASE_LANG + "general.neighborchangelog");
		onlyKillNormal = config.getBoolean("onlykillnormal", Configuration.CATEGORY_GENERAL, true,
				"", BASE_LANG + "general.onlykillnormal");
		coreFeedsPlayerType = Loader.isModLoaded("touchofbeacon") ? EnumPlayerType.valueOf(
				config.getString("corefeedsplayertype", Configuration.CATEGORY_GENERAL,
						EnumPlayerType.TREE_SPIRIT.name(), "", new String[]{EnumPlayerType.NORMAL.name(),
								EnumPlayerType.TREE_SPIRIT.name(), EnumPlayerType.NONE.name()},
						BASE_LANG + "general.corefeedsplayertype")) : EnumPlayerType.NONE;
		// Client
		config.setCategoryLanguageKey(Configuration.CATEGORY_CLIENT, BASE_LANG + "client");
		deathWarningType = EnumDeathWarning.valueOf(config.getString(
				"deathwarningtype", Configuration.CATEGORY_CLIENT, EnumDeathWarning.BAR.name(), "",
				new String[]{EnumDeathWarning.FOG.name(), EnumDeathWarning.PUMPKIN.name(),
						EnumDeathWarning.BAR.name()}, BASE_LANG + "client.deathwarningtype"));
		// Core Destruction
		config.setCategoryLanguageKey(CATEGORY_DESTRUCTION, BASE_LANG + CATEGORY_DESTRUCTION);
		zombiesDestroyCore = config.getBoolean("zombie", CATEGORY_DESTRUCTION, true, "",
				BASE_LANG + CATEGORY_DESTRUCTION + ".zombie");
		skeletonsDestroyCore = config.getBoolean("skeleton", CATEGORY_DESTRUCTION, false, "",
				BASE_LANG + CATEGORY_DESTRUCTION + ".skeleton");
		creepersDestroyCore = config.getBoolean("creeper", CATEGORY_DESTRUCTION, false, "",
				BASE_LANG + CATEGORY_DESTRUCTION + ".creeper");
		// Advanced
		config.setCategoryLanguageKey(CATEGORY_ADVANCED, BASE_LANG + CATEGORY_ADVANCED);
		worldTypeSkyTreeDefault = config.getBoolean("WorldTypeSkyTree", CATEGORY_ADVANCED, false,
				"", BASE_LANG + CATEGORY_ADVANCED + ".worldtypeskytree");
		config.save();
	}

	public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(MODID)) {
			Config.config.save();
			Config.load();
		}
	}

	public static Configuration getConfig() {
		return config;
	}

	public static ArrayList<IConfigElement> getCategories() {
		ArrayList<IConfigElement> categories = new ArrayList<IConfigElement>();
		categories.addAll(new ConfigElement(getConfig().getCategory(Configuration.CATEGORY_GENERAL))
				.getChildElements());
		categories.add(new ConfigElement(getConfig().getCategory(Configuration.CATEGORY_CLIENT)));
		categories.add(new ConfigElement(getConfig().getCategory(CATEGORY_DESTRUCTION)));
		categories.add(new ConfigElement(getConfig().getCategory(CATEGORY_ADVANCED)));
		return categories;
	}
}
