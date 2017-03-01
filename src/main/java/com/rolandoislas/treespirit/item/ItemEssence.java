package com.rolandoislas.treespirit.item;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Rolando on 2/27/2017.
 */
public class ItemEssence extends Item {
	public ItemEssence() {
		this.setUnlocalizedName(TreeSpirit.MODID + ".essence");
		this.setRegistryName(TreeSpirit.MODID, "essence");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}
}
