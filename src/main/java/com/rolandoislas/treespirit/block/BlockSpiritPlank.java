package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Created by Rolando on 2/27/2017.
 */
public class BlockSpiritPlank extends Block {
	public BlockSpiritPlank() {
		super(Material.WOOD);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName(TreeSpirit.MODID + ".plank");
		this.setRegistryName(TreeSpirit.MODID, "plank");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}
}
