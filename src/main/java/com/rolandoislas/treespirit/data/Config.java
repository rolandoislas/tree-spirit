package com.rolandoislas.treespirit.data;

import com.rolandoislas.treespirit.data.spirit.EnumPlayerType;
import com.rolandoislas.treespirit.data.spirit.RootBlock;
import com.rolandoislas.treespirit.gui.renderer.EnumDeathWarning;
import com.rolandoislas.treespirit.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rolandoislas.treespirit.TreeSpirit.MODID;

/**
 * Created by Rolando on 2/21/2017.
 */
public class Config {
	private static final String BASE_LANG = MODID + ".config.";
	private static final String CATEGORY_DESTRUCTION = "destruction";
	private static final String CATEGORY_ADVANCED = "advanced";
    private static final String CATEGORY_RESTART = "restart";
	private static final String[] defaultRootBlocks;
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
	public static boolean playerDeathDestroysCore;
	public static boolean enableSkyTreeWorld;
	public static boolean growCropsAroundPlayer;
	public static boolean giveSaplingOnSpawn;
	private static String[] rootBlocksStrings;
	public static List<RootBlock> rootBlocks;
	public static int lifeExtenderMaxSeconds;

	static {
		defaultRootBlocks = new String[] {
				ModBlocks.LOG.getRegistryName().toString(),
				ModBlocks.CORE.getRegistryName().toString(),
				ModBlocks.LEAF.getRegistryName().toString()
		};
		rootBlocks = new ArrayList<>();
	}

	public static void setConfigFile(File configFile) {
		config = new Configuration(configFile);
	}

	public static void load() {
		config.load();
		// General
		config.setCategoryLanguageKey(Configuration.CATEGORY_GENERAL, BASE_LANG + "general");
		deathTime = config.getInt("deathtime", Configuration.CATEGORY_GENERAL, 10, 1,
				Integer.MAX_VALUE / 40, "", BASE_LANG + "general.deathtime");
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
		growCropsAroundPlayer = config.getBoolean("GrowCropsAroundPlayer", Configuration.CATEGORY_GENERAL, false,
				"", BASE_LANG + Configuration.CATEGORY_GENERAL + ".grow_crops_around_player");
		giveSaplingOnSpawn = config.getBoolean("give_sapling_on_spawn", Configuration.CATEGORY_GENERAL,
				false, "", BASE_LANG + "general.give_sapling_on_spawn");
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
		playerDeathDestroysCore = config.getBoolean("PlayerDeathDestroysCore", CATEGORY_ADVANCED, true,
				"", BASE_LANG + CATEGORY_ADVANCED + ".player_death_destroys_core");
		rootBlocksStrings = config.getStringList("root_blocks", CATEGORY_ADVANCED,
				defaultRootBlocks,
				"", new String[]{}, BASE_LANG + CATEGORY_ADVANCED + ".root_blocks");
		generateRootBlocks();
		// Restart Required
        config.setCategoryLanguageKey(CATEGORY_RESTART, BASE_LANG + CATEGORY_RESTART);
        config.setCategoryRequiresMcRestart(CATEGORY_RESTART, true);
		enableSkyTreeWorld = config.getBoolean("EnableWorldTypeSkyTree", CATEGORY_RESTART, false,
				"", BASE_LANG + CATEGORY_RESTART + ".enable_world_type_sky_tree");
        lifeExtenderMaxSeconds = config.getInt("lifeExtenderMaxSeconds",
                CATEGORY_RESTART, 60 * 5, 1, 65000 / 40,
                "", BASE_LANG + CATEGORY_RESTART  + ".lifeExtenderMaxSeconds");
		config.save();
	}

	/**
	 * Generate a list of root blocks based on the strings provided
	 */
	private static void generateRootBlocks() {
		Pattern blockPattern = Pattern.compile("(\\w+:\\w+)(?:\\s(\\d+)(?:\\s(\\d+))?)?");
		rootBlocks.clear();
		for (String rootBlockString : rootBlocksStrings) {
			Matcher matcher = blockPattern.matcher(rootBlockString);
			if (!matcher.matches())
				continue;
			String name = matcher.group(1);
			Integer meta = null;
			if (matcher.groupCount() > 2) {
			    String metaString = matcher.group(2);
			    try {
			        meta = Integer.parseInt(metaString);
                }
                catch (NumberFormatException ignore) {}
            }
            // Attempt to create the block
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
			if (block == null || block.equals(Blocks.AIR))
				continue;
			RootBlock rootBlock = new RootBlock(block);
			if (meta != null)
				rootBlock.setMeta(meta);
			rootBlocks.add(rootBlock);
		}
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
		categories.add(new ConfigElement(getConfig().getCategory(CATEGORY_RESTART)));
		return categories;
	}
}
