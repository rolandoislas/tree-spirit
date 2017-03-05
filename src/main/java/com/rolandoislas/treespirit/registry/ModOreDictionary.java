package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.TreeSpirit;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Rolando on 2/27/2017.
 */
public class ModOreDictionary {
	public static void register() {
		registerItem(ModItems.LOG, "treeWood");
		registerItem(ModItems.SAPLING, "treeSapling");
		registerItem(ModItems.PLANK, "plankWood");
		registerItem(ModItems.GRASS, "blockGrass");
		registerItem(ModItems.LEAF, "treeLeaves");
		registerItem(ModItems.ESSENCE, "essence");
		registerItem(ModItems.LIFE_EXTENDER, "utility");
		registerItem(ModItems.ROOM_SEALER, "utility");
		registerItem(ModItems.DOOR, "door");
		registerItem(ModItems.DEV_TOOL, "utility");
	}

	private static void registerItem(Item itemIn, String name) {
		NonNullList<ItemStack> subItems = NonNullList.create();
		itemIn.getSubItems(itemIn, ModCreativeTabs.MAIN, subItems);
		for (ItemStack item : subItems) {
			OreDictionary.registerOre(name, item);
			String subName = getOreDictNameFromItem(item);
			OreDictionary.registerOre(subName, item);
			TreeSpirit.logger.debug("Registered Ore Dict Name: " + subName);
		}
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
