package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.data.spirit.SpiritCore;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.registry.ModItems;
import com.rolandoislas.treespirit.tileentity.TileEntitySpiritCore;
import com.rolandoislas.treespirit.util.InfoUtil;
import com.rolandoislas.treespirit.util.JsonUtil;
import com.rolandoislas.treespirit.util.SpiritUtil;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Rolando on 2/28/2017.
 */
public class BlockSpiritCore extends BlockRotatedPillar implements ITileEntityProvider {
	public BlockSpiritCore() {
		super(Material.WOOD);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		this.setLightLevel(1);
		this.setTickRandomly(true);

		this.setUnlocalizedName(TreeSpirit.MODID + ".core");
		this.setRegistryName(TreeSpirit.MODID, "core");
		this.setCreativeTab(ModCreativeTabs.MAIN);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSpiritSapling.TYPE, EnumWood.ELDER)
				.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockSpiritSapling.TYPE, BlockRotatedPillar.AXIS);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumWood sapling : EnumWood.values())
			list.add(new ItemStack(itemIn, 1, sapling.getMeta()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockSpiritSapling.TYPE, EnumWood.NORMAL.getFromMeta(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockSpiritSapling.TYPE).getMeta();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(BlockSpiritSapling.TYPE).getMeta();
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return plantable instanceof BlockSpiritSapling;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
								ItemStack stack) {
		if (worldIn.isRemote)
			return;
		if (!(placer instanceof EntityPlayer))
			return;
		SpiritUtil.sendMessage((EntityPlayer)placer, Messages.CORE_PLACED_BY_PLAYER);
		this.dropBlockAsItem(worldIn, pos, state, 0);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
			return;
		switch (EnumWood.NORMAL.getFromMeta(getMetaFromState(state))) {
			case ELDER:
				SpiritUtil.removeCore(worldIn, pos);
				break;
			case DIMENSION:
				SpiritUtil.removeDimensionCore(worldIn, pos, "");
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (SpiritUtil.getOwnerId(worldIn, pos).isEmpty())
			worldIn.setBlockToAir(pos);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySpiritCore();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
									EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;
		if (!hand.equals(EnumHand.MAIN_HAND))
			return false;
		// Check action
		boolean isOwner = SpiritUtil.getOwnerId(worldIn, pos).equals(InfoUtil.getPlayerUuid(playerIn));
		boolean hasCoreInHand = ItemStack.areItemsEqual(playerIn.getHeldItemMainhand(),
				ModItems.CORE.getDefaultInstance());
		boolean shiftingWithEmptyHand = playerIn.getHeldItemMainhand().isEmpty() && playerIn.isSneaking();
		// Upgrade
		if (hasCoreInHand && isOwner){
			int level = SpiritUtil.upgradeCore(worldIn, pos);
			if (level > -1) {
				playerIn.getHeldItemMainhand().shrink(1);
				SpiritUtil.sendMessage(playerIn, Messages.CORE_UPGRADE_PASS, level + 1);
			}
			else
				SpiritUtil.sendMessage(playerIn, Messages.CORE_UPGRADE_FAIL, level + 1);
			return true;
		}
		// Downgrade
		else if (shiftingWithEmptyHand && isOwner) {
			int level = SpiritUtil.downgradeCore(worldIn, pos);
			if (level > -1) {
				playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.CORE, 1, 0));
				SpiritUtil.sendMessage(playerIn, Messages.CORE_DOWNGRADE_PASS, level + 1);
			}
			else
				SpiritUtil.sendMessage(playerIn, Messages.CORE_DOWNGRADE_FAIL, level + 1);
			return true;
		}
		// Player tried to up/downgrade a core that is not theirs
		else if (!isOwner && (hasCoreInHand || shiftingWithEmptyHand)) {
			SpiritUtil.sendMessage(playerIn, Messages.CORE_NOT_OWNER);
			return true;
		}
		// Normal action
		else
			return false;
	}
}
