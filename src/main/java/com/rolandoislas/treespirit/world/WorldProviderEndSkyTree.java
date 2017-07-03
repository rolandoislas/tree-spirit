package com.rolandoislas.treespirit.world;

import com.rolandoislas.treespirit.registry.WorldTypes;
import com.rolandoislas.treespirit.world.gen.ChunkProviderSkyTree;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.IChunkGenerator;

/**
 * Created by Rolando on 3/8/2017.
 */
public class WorldProviderEndSkyTree extends WorldProviderEnd {
	@Override
	public IChunkGenerator createChunkGenerator() {
		if (world.getWorldInfo().getTerrainType() == WorldTypes.skyTree)
			return new ChunkProviderSkyTree(world, "");
		else
			return super.createChunkGenerator();
	}
}
