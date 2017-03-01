package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.data.spirit.SpiritCore;
import com.rolandoislas.treespirit.data.spirit.SpiritSapling;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.util.InfoUtil;
import com.rolandoislas.treespirit.util.JsonUtil;
import com.rolandoislas.treespirit.util.SpiritUtil;
import com.rolandoislas.treespirit.util.WorldUtil;
import net.minecraft.block.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Rolando on 2/27/2017.
 */
public class BlockSpiritSapling extends BlockBush implements IGrowable {
	private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
	private static final PropertyEnum<EnumWood> TYPE = PropertyEnum.create("sapling", EnumWood.class);

	public BlockSpiritSapling() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0)
				.withProperty(TYPE, EnumWood.NORMAL));
		this.setSoundType(SoundType.PLANT);
		this.setUnlocalizedName(TreeSpirit.MODID + ".sapling");
		this.setRegistryName(TreeSpirit.MODID, "sapling");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}

	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, STAGE);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumWood sapling : EnumWood.values())
			list.add(new ItemStack(itemIn, 1, sapling.getMeta()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, EnumWood.NORMAL.getFromMeta(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getMeta();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(TYPE).getMeta();
	}

	@Override
	public boolean isToolEffective(String type, IBlockState state) {
		return type.equals("axe");
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return worldIn.rand.nextFloat() < 0.45;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
			return;
		if (state.getValue(STAGE) == 0)
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		else
			this.generateTree(worldIn, pos, state, rand);
	}

	private void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote)
			return;
		int height = 5;
		// ~50% to add up to 2 more to height
		if (rand.nextFloat() < .5)
			height += rand.nextInt(3);
		// Set the wood blocks
		for (int upAmount = 0; upAmount < height; upAmount++) {
			if (worldIn.getBlockState(pos.up(upAmount)).getBlock() != Blocks.AIR)
				continue;
			worldIn.setBlockState(pos.up(upAmount), ModBlocks.LOG.getDefaultState()
					.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
		}
		// Create leaves
		int leafHeight = (int) Math.round(height * .4);
		IBlockState leafState = ModBlocks.LEAF.getDefaultState().withProperty(BlockLeaves.DECAYABLE, true)
				.withProperty(BlockLeaves.CHECK_DECAY, true);
		WorldUtil.setBlocksAroundPos(pos.up(height), worldIn, 1, 0, 1, leafState,
				true, true, 0);
		for (int leafIndex = 1; leafIndex <= leafHeight; leafIndex++)
			WorldUtil.setBlocksAroundPos(pos.up(height - leafIndex), worldIn, 2, 0, 2, leafState,
					false, true, (leafIndex == 1 || leafIndex == leafHeight) ? 5 : 0);
		// Spawn the elder core
		boolean isElder = state.getBlock().getMetaFromState(state) == EnumWood.ELDER.getMeta();
		SpiritSapling sapling =
				JsonUtil.getSpiritData().getWorld(InfoUtil.getWorldId(worldIn)).getSapling(worldIn, pos);
		SpiritCore core = JsonUtil.getSpiritData().getWorld(InfoUtil.getWorldId(worldIn))
				.getCore(sapling.getPlayerId());
		if (isElder && core.getDimension() == null) {
			SpiritUtil.registerCore(worldIn, pos);
			worldIn.setBlockState(pos, ModBlocks.CORE.getDefaultState()
					.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
		}
		// Normal
		else
			worldIn.setBlockState(pos, ModBlocks.LOG.getDefaultState()
					.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
				this.grow(worldIn, rand, pos, state);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
								ItemStack stack) {
		if (worldIn.isRemote)
			return;
		if (!(placer instanceof EntityPlayer))
			return;
		if (EnumWood.NORMAL.getFromMeta(getMetaFromState(state)) == EnumWood.ELDER) {
			SpiritUtil.registerSapling(worldIn, pos, (EntityPlayer) placer);
			SpiritUtil.sendMessage((EntityPlayer) placer, Messages.ELDER_SAPLING_PLACED, placer.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		if (worldIn.isRemote)
			return;
		if (EnumWood.NORMAL.getFromMeta(getMetaFromState(state)) == EnumWood.ELDER)
			SpiritUtil.removeSapling(worldIn, pos);
	}
}
