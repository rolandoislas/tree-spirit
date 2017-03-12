package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Rolando on 3/12/2017.
 */
public class BlockSpiritAir extends BlockAir {
	public BlockSpiritAir() {
		this.setUnlocalizedName(TreeSpirit.MODID + ".air");
		this.setRegistryName(TreeSpirit.MODID, "air");
	}

	@Override
	public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return super.getCollisionBoundingBox(blockState, worldIn, pos);
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
		return PathNodeType.WALKABLE;
	}
}
