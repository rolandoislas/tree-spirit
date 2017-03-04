package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.tileentity.TileEntitySpiritCore;
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
		worldIn.setBlockToAir(pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
			return;
		switch (EnumWood.NORMAL.getFromMeta(getMetaFromState(state))) {
			case ELDER:
				SpiritUtil.removeCore(worldIn, pos, "");
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
}
