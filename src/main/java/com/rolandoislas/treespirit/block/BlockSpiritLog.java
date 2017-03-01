package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Rolando on 2/27/2017.
 */
public class BlockSpiritLog extends BlockRotatedPillar {
	public BlockSpiritLog() {
		super(Material.WOOD);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		this.setTickRandomly(Config.randomTicksLog);

		this.setUnlocalizedName(TreeSpirit.MODID + ".log");
		this.setRegistryName(TreeSpirit.MODID, "log");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
		checkDestroy(world, observerPos);
	}

	private boolean checkDestroy(World world, BlockPos pos) {
		if (world.isRemote)
			return false;
		if (!WorldUtil.hasBlockNearby(pos, world, 1, 1, 1,
				Integer.MAX_VALUE, ModBlocks.CORE) &&
				!WorldUtil.hasBlockNearby(pos, world, 1, 10, 1, 0,
						ModBlocks.CORE)) {
			world.setBlockToAir(pos);
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;
		if (checkDestroy(worldIn, pos)) {
			if (placer instanceof EntityPlayer) {
				((EntityPlayer) placer).inventory.
						addItemStackToInventory(new ItemStack(stack.getItem(), 1, stack.getMetadata()));
				((EntityPlayer) placer).inventory.markDirty();
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		checkDestroy(worldIn, pos);
	}
}
