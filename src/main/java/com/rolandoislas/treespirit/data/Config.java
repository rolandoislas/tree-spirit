package com.rolandoislas.treespirit.data;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

import java.io.File;
import java.util.ArrayList;

import static com.rolandoislas.treespirit.TreeSpirit.MODID;

/**
 * Created by Rolando on 2/21/2017.
 */
public class Config {
	private static final String BASE_LANG = MODID + ".config.";
	private static Configuration config;
	public static int deathTime;
	public static boolean randomTicksLog;

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
		return categories;
	}
}
