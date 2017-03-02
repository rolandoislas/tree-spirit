package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.util.InfoUtil;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

/**
 * Created by Rolando on 2/27/2017.
 */
public class BlockSpiritSapling extends BlockBush implements IGrowable {
	private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
	static final PropertyEnum<EnumWood> TYPE = PropertyEnum.create("type", EnumWood.class);

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

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return false;
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
		// Spawn the core
		String owner = SpiritUtil.getOwnerId(worldIn, pos);
		EnumWood type = EnumWood.NORMAL.getFromMeta(getMetaFromState(state));
		boolean canSpawnAdditionalDimensionCores = true;
		if (Config.oneDimensionCorePerWorld)
			canSpawnAdditionalDimensionCores = !SpiritUtil.playerHasDimensionCoreInCurrentDimension(worldIn, owner);
		switch (type) {
			case ELDER:
				if (!owner.isEmpty() && !SpiritUtil.playerHasCore(worldIn, owner)) {
					SpiritUtil.registerCore(worldIn, pos);
					worldIn.setBlockState(pos, ModBlocks.CORE.getDefaultState()
							.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
				}
				break;
			case DIMENSION:
				if (!owner.isEmpty() && SpiritUtil.playerHasCore(worldIn, owner) && canSpawnAdditionalDimensionCores) {
					SpiritUtil.registerDimensionCore(worldIn, pos);
					worldIn.setBlockState(pos, ModBlocks.CORE.getDefaultState()
							.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y)
							.withProperty(TYPE, EnumWood.DIMENSION));
				}
				break;
		}
		// Normal block
		if (worldIn.getBlockState(pos).getBlock() == ModBlocks.SAPLING)
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
		String owner = InfoUtil.getPlayerUuid((EntityPlayer) placer);
		boolean hasCore = SpiritUtil.playerHasCore(worldIn, owner);
		boolean canSpawnAdditionalDimensionCores = true;
		if (Config.oneDimensionCorePerWorld)
			canSpawnAdditionalDimensionCores = !SpiritUtil.playerHasDimensionCoreInCurrentDimension(worldIn, owner);
		switch (EnumWood.NORMAL.getFromMeta(getMetaFromState(state))) {
			case ELDER:
				if (!hasCore) {
					SpiritUtil.registerSapling(worldIn, pos, (EntityPlayer) placer);
					SpiritUtil.sendMessage((EntityPlayer) placer, Messages.ELDER_SAPLING_PLACED,
							placer.getDisplayName());
				}
				else
					SpiritUtil.sendMessage((EntityPlayer) placer,
							Messages.CORE_PLACE_FAILED, placer.getDisplayName());
				break;
			case DIMENSION:
				if (hasCore) {
					if (canSpawnAdditionalDimensionCores) {
						SpiritUtil.registerSapling(worldIn, pos, (EntityPlayer) placer);
						SpiritUtil.sendMessage((EntityPlayer) placer,
								Messages.DIMENSION_SAPLING_PLACED, placer.getDisplayName());
					}
					else
						SpiritUtil.sendMessage((EntityPlayer) placer,
								Messages.DIMENSION_CORE_PLACE_FAILED, placer.getDisplayName());
				}
				else
					SpiritUtil.sendMessage((EntityPlayer) placer,
							Messages.NO_CORE, placer.getDisplayName());
				break;
			case NORMAL:
				if (worldIn.getBlockState(pos.down()).getBlock() != ModBlocks.GRASS &&
						worldIn.getBlockState(pos.down()).getBlock() != ModBlocks.CORE) {
					this.dropBlockAsItem(worldIn, pos, state, 0);
					worldIn.setBlockToAir(pos);
					SpiritUtil.sendMessage((EntityPlayer) placer,
							Messages.NORMAL_SAPLING_PLANT_FAIL, placer.getDisplayName());
				}
				break;
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		if (worldIn.isRemote)
			return;
		switch (EnumWood.NORMAL.getFromMeta(getMetaFromState(state))) {
			case ELDER:
			case DIMENSION:
				SpiritUtil.removeSapling(worldIn, pos);
				break;
		}
	}
}
