package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

/**
 * Created by Rolando on 3/2/2017.
 */
public class BlockSpiritGrass extends BlockGrass {
	public BlockSpiritGrass() {
		super();
		this.setSoundType(SoundType.GROUND);
		this.setHardness(.5f);

		this.setUnlocalizedName(TreeSpirit.MODID + ".grass");
		this.setRegistryName(TreeSpirit.MODID, "grass");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		int x = pos.getX() - 1;
		int y = pos.getY() - 1;
		int z = pos.getZ() - 1;
		x += rand.nextInt(3);
		y += rand.nextInt(3);
		z += rand.nextInt(3);
		BlockPos spreadPos = new BlockPos(x, y, z);
		Block spreadBlock = worldIn.getBlockState(spreadPos).getBlock();
		if (spreadBlock == Blocks.DIRT && !worldIn.getBlockState(spreadPos.up()).isNormalCube())
			worldIn.setBlockState(spreadPos, ModBlocks.GRASS.getDefaultState());
		// Check if this block should turn to dirt
		if (worldIn.getBlockState(pos.up()).isNormalCube())
			worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
								   IPlantable plantable) {
		return plantable instanceof BlockSpiritSapling || plantable instanceof BlockSapling;
	}

	@Override
	public boolean isToolEffective(String type, IBlockState state) {
		return type.contains("shovel") || type.contains("spade");
	}
}
