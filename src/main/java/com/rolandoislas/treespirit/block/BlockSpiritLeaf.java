package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.registry.ModItems;
import com.rolandoislas.treespirit.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rolando on 2/27/2017.
 */
public class BlockSpiritLeaf extends Block implements IShearable {
	public BlockSpiritLeaf() {
		super(Material.LEAVES);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLeaves.DECAYABLE, true)
				.withProperty(BlockLeaves.CHECK_DECAY, true));
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(TreeSpirit.MODID + ".leaf");
		this.setRegistryName(TreeSpirit.MODID, "leaf");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}

	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(item);
		return new ArrayList<ItemStack>();
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return Minecraft.isFancyGraphicsEnabled() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return !(!Minecraft.isFancyGraphicsEnabled() && blockAccess.getBlockState(pos.offset(side)).getBlock() == this)
				&& super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {
		if (!state.getValue(BlockLeaves.CHECK_DECAY))
			world.setBlockState(pos, state.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote)
			return;
		if (state.getValue(BlockLeaves.CHECK_DECAY) && state.getValue(BlockLeaves.DECAYABLE)) {
			checkDestroy(worldIn, pos);
		}
	}

	private void checkDestroy(World worldIn, BlockPos pos) {
		if (!WorldUtil.hasBlockNearby(pos, worldIn, 1, 1, 1,
				1, ModBlocks.LOG)) {
			this.destroy(worldIn, pos);
		}
	}

	private void destroy(World worldIn, BlockPos pos) {
		this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
		worldIn.setBlockToAir(pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (!(world instanceof World))
			return drops;
		if (((World) world).isRemote)
			return drops;
		Random rand = ((World)world).rand;
		if (rand.nextInt(10 - (fortune <= 10 ? fortune : 10)) == 0)
			drops.add(new ItemStack(ModItems.SAPLING));
		return drops;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public boolean isFoliage(IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 300;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;
		worldIn.setBlockState(pos, state.withProperty(BlockLeaves.CHECK_DECAY, false)
				.withProperty(BlockLeaves.DECAYABLE, false));
	}
}
