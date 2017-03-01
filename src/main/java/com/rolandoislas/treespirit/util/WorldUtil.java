package com.rolandoislas.treespirit.util;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

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

	@MethodsReturnNonnullByDefault
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
}
