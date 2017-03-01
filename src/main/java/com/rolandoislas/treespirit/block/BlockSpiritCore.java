package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.util.SpiritUtil;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

/**
 * Created by Rolando on 2/28/2017.
 */
public class BlockSpiritCore extends BlockRotatedPillar {
	public BlockSpiritCore() {
		super(Material.WOOD);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
		this.setLightLevel(1);

		this.setUnlocalizedName(TreeSpirit.MODID + ".core");
		this.setRegistryName(TreeSpirit.MODID, "core");
		this.setCreativeTab(ModCreativeTabs.MAIN);
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
		SpiritUtil.removeCore(worldIn, pos, "");
		super.breakBlock(worldIn, pos, state);
	}
}
