package com.rolandoislas.treespirit.world;

import com.rolandoislas.treespirit.registry.WorldTypes;
import com.rolandoislas.treespirit.world.gen.ChunkProviderSkyTree;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * Created by Rolando on 3/8/2017.
 */
public class WorldProviderHellSkyTree extends WorldProviderHell {
	@Override
	public IChunkGenerator createChunkGenerator() {
		if (world.getWorldInfo().getTerrainType() == WorldTypes.skyTree)
			return new ChunkProviderSkyTree(world, "");
		else
			return super.createChunkGenerator();
	}
}
