package com.rolandoislas.treespirit.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Rolando on 2/27/2017.
 */
public class ModOreDictionary {
	public static void register() {
		registerItem(ModItems.LOG, "treeWood");
		registerItem(ModItems.LOG, "logWood");
		registerItem(ModItems.SAPLING, "treeSapling");
		registerItem(ModItems.PLANK, "plankWood");
		registerItem(ModItems.GRASS, "blockGrass");
		registerItem(ModItems.LEAF, "treeLeaves");
		registerItem(ModItems.ESSENCE, "essence");
		registerItem(ModItems.LIFE_EXTENDER, "utility");
		registerItem(ModItems.ROOM_SEALER, "utility");
		registerItem(ModItems.DOOR, "door");
		registerItem(ModItems.DEV_TOOL, "utility");
		registerItem(ModItems.CORE, "utility");
		registerItem(ModItems.MUSHROOM_BUILDER, "utility");
	}

	private static void registerItem(Item itemIn, String name) {
		OreDictionary.registerOre(name, itemIn);
		OreDictionary.registerOre(getOreDictNameFromItem(new ItemStack(itemIn)), itemIn);
	}

	private static String getOreDictNameFromItem(ItemStack item) {
		String subName = "";
		String[] split = item.getUnlocalizedName().split("\\.");
		subName += "treeSpirit";
		subName += split[2].substring(0, 1).toUpperCase() + split[2].substring(1);
		subName += split.length > 3 ? split[3].substring(0, 1).toUpperCase() + split[3].substring(1) : "";
		return subName;
	}
}
