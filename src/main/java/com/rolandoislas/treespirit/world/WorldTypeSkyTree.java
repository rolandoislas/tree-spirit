package com.rolandoislas.treespirit.world;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.world.gen.ChunkProviderSkyTree;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;

/**
 * Created by Rolando on 3/7/2017.
 */
public class WorldTypeSkyTree extends WorldType {
	/**
	 * Creates a new world type, the ID is hidden and should not be referenced by modders.
	 * It will automatically expand the underlying worldType array if there are no IDs left.
	 */
	public WorldTypeSkyTree() {
		super(TreeSpirit.MODID + ".sky");
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return super.getBiomeProvider(world);
	}

	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkProviderSkyTree(world, generatorOptions);
	}

	@Override
	public int getSpawnFuzz(WorldServer world, MinecraftServer server) {
		return 1;
	}
}
