package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Created by Rolando on 3/5/2017.
 */
public class BlockSpiritDoor extends BlockDoor {
	public BlockSpiritDoor() {
		super(Material.WOOD);
		this.setHardness(1.5f);
		this.setSoundType(SoundType.WOOD);
		this.setUnlocalizedName(TreeSpirit.MODID + ".door");
		this.setRegistryName(TreeSpirit.MODID, "door");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}
}
