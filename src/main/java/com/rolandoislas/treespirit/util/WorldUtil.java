package com.rolandoislas.treespirit.util;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Messages;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.ArrayList;

/**
 * Created by Rolando on 2/27/2017.
 */
public class WorldUtil {
	private static boolean hasBlockNearby(BlockPos pos, World worldIn, int radiusX, int radiusY,
										  int radiusZ, int neighborCheckDepth, ArrayList<int[]> triedBlocks,
										  Block... blocks) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if (!worldIn.isAreaLoaded(new BlockPos(x - radiusX, y - radiusY, z - radiusZ),
				new BlockPos(x + radiusX, y + radiusY, z + radiusZ)))
			return true;
		Block blockType = worldIn.getBlockState(pos).getBlock();
		ArrayList<int[]> blockPositions = getBlocksPositionsAroundPos(pos, radiusX, radiusY, radiusZ, false);
		for (int posIndex = 0; posIndex < blockPositions.size(); posIndex++) {
			int[] blockPos = blockPositions.get(posIndex);
			for (int[] tried : triedBlocks) {
				if (blockPos[0] == tried[0] && blockPos[1] == tried[1] && blockPos[2] == tried[2]) {
					blockPositions.remove(posIndex);
					posIndex--;
					break;
				}
			}
		}
		for (int[] blockPos : blockPositions) {
			triedBlocks.add(blockPos);
			Block block = worldIn.getBlockState(new BlockPos(blockPos[0], blockPos[1], blockPos[2])).getBlock();
			// Check if the block is of the type passed
			for (Block lookingFor : blocks)
				if (block == lookingFor)
					return true;
		}
		if (neighborCheckDepth <= 0)
			return false;
		for (int[] blockPos : blockPositions) {
			Block block = worldIn.getBlockState(new BlockPos(blockPos[0], blockPos[1], blockPos[2])).getBlock();
			// Search around neighbors
			if (block == blockType &&
					hasBlockNearby(new BlockPos(blockPos[0], blockPos[1], blockPos[2]),
							worldIn, radiusX, radiusY, radiusZ,
							neighborCheckDepth - 1, triedBlocks, blocks))
				return true;
		}
		return false;
	}

	public static boolean hasBlockNearby(BlockPos pos, World worldIn, int radiusX, int radiusY, int radiusZ,
										 int neighborCheckDepth, Block... blocks) {
		return hasBlockNearby(pos, worldIn, radiusX, radiusY, radiusZ, neighborCheckDepth, new ArrayList<int[]>(),
				blocks);
	}

	public static void setBlocksAroundPos(BlockPos pos, World worldIn, int radiusX, int radiusY,
										  int radiusZ, IBlockState state,
										  boolean includeOrigin, boolean onlyReplaceAir, int removeRandomly) {
		ArrayList<int[]> blockPositions = getBlocksPositionsAroundPos(pos, radiusX, radiusY, radiusZ, includeOrigin);
		for (int removes = 0; removes < removeRandomly; removes++)
			blockPositions.remove(worldIn.rand.nextInt(blockPositions.size()));
		for (int[] blockPos : blockPositions) {
			BlockPos setPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			if (onlyReplaceAir &&
					worldIn.getBlockState(setPos).getBlock() != Blocks.AIR)
				continue;
			worldIn.setBlockState(setPos, state);
		}
	}

	private static ArrayList<int[]> getBlocksPositionsAroundPos(BlockPos pos, int radiusX, int radiusY,
																int radiusZ, boolean includeOrigin) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int startX = x - radiusX;
		int startY = y - radiusY;
		int startZ = z - radiusZ;
		int endX = x + radiusX;
		int endY = y + radiusY;
		int endZ = z + radiusZ;
		ArrayList<int[]> blockPositions = new ArrayList<int[]>();
		for (int currentY = startY; currentY <= endY; currentY++) {
			for (int currentZ = startZ; currentZ <= endZ; currentZ++) {
				for (int currentX = startX; currentX <= endX; currentX++) {
					if (!includeOrigin && currentX == x && currentY == y && currentZ == z)
						continue;
					blockPositions.add(new int[]{currentX, currentY, currentZ});
				}
			}
		}
		return blockPositions;
	}

	public static EntityPlayer getPlayer(String playerId) {
		if (playerId.isEmpty())
			return null;
		// Find player
		for (World world : DimensionManager.getWorlds())
			for (EntityPlayer player : world.playerEntities)
				if (EntityPlayer.getUUID(player.getGameProfile()).toString().equals(playerId))
					return player;
		return null;
	}

	/**
	 * Gets a world with the specified dimension and tries to force load at teh specified block
	 * @param fallback world to use for forced chunk loading
	 * @param pos position of block that needs chunk loaded
	 * @param dimension dimension of the world
	 * @return world wih hopefully a loaded chunk
	 */
	public static World getWorldWithLoadedChunk(World fallback, BlockPos pos , int dimension) {
		World world = DimensionManager.getWorld(dimension);
		if (world == null) {
			world = fallback;
			world.provider.setDimension(dimension);
			ForgeChunkManager.Ticket ticket =
					ForgeChunkManager.requestTicket(TreeSpirit.instance, fallback, ForgeChunkManager.Type.NORMAL);
			if (ticket == null) {
				TreeSpirit.logger.info(I18n.format(Messages.CHUNK_LOAD_ERROR));
				return fallback;
			}
			TreeSpirit.logger.info(I18n.format(Messages.CHUNK_LOAD_ERROR));
			ForgeChunkManager.forceChunk(ticket, new ChunkPos(pos));
			ForgeChunkManager.releaseTicket(ticket);
		}
		return world;
	}

	public static boolean hasBlocksNearbyViaChain(BlockPos position, World world, int radiusX, int radiusY, int radiusZ,
												  int checkDepth, Block chain, Block... blocks) {
		ArrayList<int[]> around = getBlocksPositionsAroundPos(position, radiusX, radiusY, radiusZ, false);
		for (int[] pos : around) {
			BlockPos blockPos = new BlockPos(pos[0], pos[1], pos[2]);
			Block block = world.getBlockState(blockPos).getBlock();
			for (Block checkBlock : blocks)
				if (checkBlock == block)
					return true;
			if (block == chain &&
					hasBlockNearby(blockPos, world, 1, 1, 1, checkDepth, blocks))
				return true;
		}
		return false;
	}

	/**
	 * Search for a block within the radius - includes origin
	 * @param pos origin
	 * @param world world
	 * @param radiusX how far x
	 * @param radiusY how far y
	 * @param radiusZ how far z
	 * @param block block type to match
	 * @return position or null
	 */
	public static BlockPos getBlockNearby(BlockPos pos, World world, int radiusX, int radiusY, int radiusZ, Block block) {
		ArrayList<int[]> blockPositions = getBlocksPositionsAroundPos(pos, radiusX, radiusY, radiusZ, true);
		for (int[] blockPos : blockPositions) {
			BlockPos checkBlockPos = new BlockPos(blockPos[0], blockPos[1], blockPos[2]);
			if (world.getBlockState(checkBlockPos).getBlock() == block)
				return checkBlockPos;
		}
		return null;
	}

	/**
	 * Grow crops around a block
	 * @param position block
	 * @param world world of block
	 */
	public static void growCropsAround(BlockPos position, World world) {
		BlockPos pos = position.west().north();
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				BlockPos p = pos.east(x).south(z);
				IBlockState state = world.getBlockState(p);
				Block block = state.getBlock();
				if (block instanceof IGrowable && world.rand.nextFloat() < .05)
					((IGrowable) block).grow(world, world.rand, p, state);
			}
		}
	}
}
