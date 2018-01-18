package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.registry.ModItems;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

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

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(getItem());
	}

	private Item getItem() {
		return ModItems.DOOR;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF).equals(BlockDoor.EnumDoorHalf.UPPER) ? Items.AIR : getItem();
	}
}
