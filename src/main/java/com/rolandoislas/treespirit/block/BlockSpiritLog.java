package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.data.spirit.RootBlock;
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
		if (Config.neighborChangeLog)
			checkDestroy(world, observerPos, observerState, false);
	}

	private boolean checkDestroy(World world, BlockPos pos, IBlockState state, boolean dropBlock) {
		if (world.isRemote)
			return false;
		Block[] rootBlocks = new Block[Config.rootBlocks.size()];
		for (RootBlock rootBlock : Config.rootBlocks)
			rootBlocks[Config.rootBlocks.indexOf(rootBlock)] = rootBlock.getBlock();
		if (
				// Can block be sustained by radius of core
				!WorldUtil.hasBlockNearby(pos, world, 1, 1, 1,
						Integer.MAX_VALUE, rootBlocks, ModBlocks.CORE) &&
				// Can tree be sustained by core or grass
				!WorldUtil.hasBlockNearby(pos, world, 0, 10, 0, 0,
						ModBlocks.CORE, ModBlocks.GRASS)
				) {
			if (dropBlock)
				this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;
		checkDestroy(worldIn, pos, state,
				placer instanceof EntityPlayer && !((EntityPlayer) placer).isCreative());
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		checkDestroy(worldIn, pos, state, false);
	}
}
