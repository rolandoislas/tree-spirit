package com.rolandoislas.treespirit.world;

import com.rolandoislas.treespirit.registry.WorldTypes;
import com.rolandoislas.treespirit.world.gen.ChunkProviderSkyTree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * Created by Rolando on 3/8/2017.
 */
public class WorldProviderSurfaceSkyTree extends WorldProviderSurface {
	@Override
	public IChunkGenerator createChunkGenerator() {
		if (world.getWorldInfo().getTerrainType() == WorldTypes.skyTree)
			return new ChunkProviderSkyTree(world, "");
		else
			return super.createChunkGenerator();
	}

	@Override
	public BlockPos getRandomizedSpawnPoint() {
		if (world.getWorldInfo().getTerrainType() == WorldTypes.skyTree)
			return new BlockPos(1, 64, 1);
		else
			return super.getRandomizedSpawnPoint();
	}
}
