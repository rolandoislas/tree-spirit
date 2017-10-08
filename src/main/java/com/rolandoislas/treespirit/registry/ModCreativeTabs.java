package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.TreeSpirit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by Rolando on 2/23/2017.
 */
public class ModCreativeTabs {
	public static final CreativeTabs MAIN = new CreativeTabs(CreativeTabs.getNextID(), TreeSpirit.MODID) {
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.DEV_TOOL);
		}
	};
}
