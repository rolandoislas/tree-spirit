package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.data.spirit.RootBlock;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.util.SpiritUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Rolando on 3/3/2017.
 */
public class BlockRoomSealer extends BlockBush {
	private static final int MAX_SIZE = 50;

	public BlockRoomSealer() {
		super(Material.PLANTS);
		this.setUnlocalizedName(TreeSpirit.MODID + ".room_sealer");
		this.setRegistryName(TreeSpirit.MODID, "room_sealer");
		this.setCreativeTab(ModCreativeTabs.MAIN);
		this.setHardness(1);
		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;
		if (checkRoom(worldIn, pos))
			SpiritUtil.sendMessage(playerIn, Messages.ROOM_SEALER_PASS);
		else
			SpiritUtil.sendMessage(playerIn, Messages.ROOM_SEALER_FAIL);
		return true;
	}

	private boolean checkRoom(World worldIn, BlockPos pos) {
		boolean isSealed = false;
		AxisAlignedBB floor = getFace(worldIn, pos.down(), EnumFacing.DOWN);
		AxisAlignedBB wallWest = getFace(worldIn, new BlockPos(floor.minX, floor.minY, floor.minZ), EnumFacing.WEST);
		AxisAlignedBB wallEast = getFace(worldIn, new BlockPos(floor.maxX, floor.minY, floor.minZ), EnumFacing.EAST);
		AxisAlignedBB wallNorth = getFace(worldIn, new BlockPos(floor.minX, floor.minY, floor.minZ), EnumFacing.NORTH);
		AxisAlignedBB wallSouth = getFace(worldIn, new BlockPos(floor.minX, floor.minY, floor.maxZ), EnumFacing.SOUTH);
		AxisAlignedBB ceiling = getFace(worldIn, new BlockPos(floor.minX, wallWest.maxY, floor.minZ), EnumFacing.UP);
		if (isFullFace(worldIn, floor) && isFullFace(worldIn, wallWest) && isFullFace(worldIn, wallEast) &&
				isFullFace(worldIn, wallNorth) && isFullFace(worldIn, wallSouth) && isFullFace(worldIn, ceiling))
			isSealed = true;
		AxisAlignedBB dimensions = new AxisAlignedBB(floor.minX, floor.minY, floor.minZ,
				floor.maxX, ceiling.maxY, floor.maxZ);
		SpiritUtil.setRoomSealerStatus(worldIn, pos, isSealed, dimensions);
		return isSealed;
	}

	private AxisAlignedBB getFace(World worldIn, BlockPos pos, EnumFacing direction) {
		int[] downUp = getDownUp(worldIn, pos);
		int[] northSouth = getNorthSouth(worldIn, pos);
		int[] westEast = getWestEast(worldIn, pos);
		switch (direction) {
			case WEST:
			case EAST:
				return new AxisAlignedBB(pos.getX(), downUp[0], northSouth[0],
										 pos.getX(), downUp[1], northSouth[1]);
			case NORTH:
			case SOUTH:
				return new AxisAlignedBB(westEast[0], downUp[0], pos.getZ(),
										 westEast[1], downUp[1], pos.getZ());
			case DOWN:
			case UP:
				return new AxisAlignedBB(westEast[0], pos.getY(), northSouth[0],
										 westEast[1], pos.getY(), northSouth[1]);
		}
		return new AxisAlignedBB(pos);
	}

	/**
	 * Get the bounds of a face
	 * @param worldIn world
	 * @param pos origin block position
	 * @return min and max y
	 */
	private int[] getDownUp(World worldIn, BlockPos pos) {
		int down = pos.getY();
		for (int y = 1; y <= MAX_SIZE; y++) {
			if (!isCompatibleBlock(worldIn.getBlockState(pos.down(y)).getBlock()))
				break;
			down--;
		}
		int up = pos.getY();
		for (int y = 1; y <= MAX_SIZE - (pos.getY() - down); y++) {
			if (!isCompatibleBlock(worldIn.getBlockState(pos.up(y)).getBlock()))
				break;
			up++;
		}
		return new int[] {down, up};
	}

	/**
	 * Get the bounds of a face
	 * @param worldIn world
	 * @param pos origin block position
	 * @return min and max x
	 */
	private int[] getWestEast(World worldIn, BlockPos pos) {
		int west = pos.getX();
		for (int x = 1; x <= MAX_SIZE; x++) {
			if (!isCompatibleBlock(worldIn.getBlockState(pos.west(x)).getBlock()))
				break;
			west--;
		}
		int east = pos.getX();
		for (int x = 1; x <= MAX_SIZE - (pos.getX() - west); x++) {
			if (!isCompatibleBlock(worldIn.getBlockState(pos.east(x)).getBlock()))
				break;
			east++;
		}
		return new int[]{west, east};
	}

	/**
	 * Get the bounds of a face
	 * @param worldIn world
	 * @param pos origin block position
	 * @return min and max z
	 */
	private int[] getNorthSouth(World worldIn, BlockPos pos) {
		int north = pos.getZ();
		for (int z = 1; z <= MAX_SIZE; z++) {
			if (!isCompatibleBlock(worldIn.getBlockState(pos.north(z)).getBlock()))
				break;
			north--;
		}
		int south = pos.getZ();
		for (int z = 1; z <= MAX_SIZE - (pos.getZ() - north); z++) {
			if (!isCompatibleBlock(worldIn.getBlockState(pos.south(z)).getBlock()))
				break;
			south++;
		}
		return new int[]{north, south};
	}

	private boolean isFullFace(World worldIn, AxisAlignedBB face) {
		// Check face has an appropriate area
		double area = (face.maxX - face.minX + 1) * (face.maxY - face.minY + 1) * (face.maxZ - face.minZ + 1);
		if (area < 25)
			return false;
		// Check the face is made up of logs
		BlockPos startPos = new BlockPos(face.minX, face.minY, face.minZ);
		for (int y = 0; y <= face.maxY - face.minY; y++)
			for (int z = 0; z <= face.maxZ - face.minZ; z++)
				for (int x = 0; x <= face.maxX - face.minX; x++)
					if (!isCompatibleBlock(worldIn.getBlockState(startPos.east(x).south(z).up(y)).getBlock()))
						return false;
		return true;
	}

	private boolean isCompatibleBlock(Block block) {
		for (RootBlock rootBlock : Config.rootBlocks)
			if (Block.isEqualTo(block, rootBlock.getBlock()))
				return true;
		return block == ModBlocks.DOOR || block == ModBlocks.LOG;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (worldIn.isRemote)
			return;
		if (!(placer instanceof EntityPlayer))
			return;
		SpiritUtil.registerRoomSealer(worldIn, pos, (EntityPlayer) placer);
		onBlockActivated(worldIn, pos, state, (EntityPlayer) placer, EnumHand.MAIN_HAND, EnumFacing.DOWN, pos.getX(),
				pos.getY(), pos.getZ());
	}

	@Override
	public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
		if (world.isRemote)
			return;
		checkRoom(world, observerPos);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return isCompatibleBlock(worldIn.getBlockState(pos.down()).getBlock());
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == ModBlocks.LOG;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		if (worldIn.isRemote)
			return;
		SpiritUtil.removeRoomSealer(worldIn, pos);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		checkRoom(worldIn, pos);
	}
}
