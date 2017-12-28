package com.rolandoislas.treespirit.world.gen;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Rolando on 3/7/2017.
 */
public class ChunkProviderSkyTree implements IChunkGenerator {
	private final World world;
	private MapGenNetherBridge genNetherBridge;
	private WorldGenSpikes spikes;
	private MapGenEndCity endCityGen;

	public ChunkProviderSkyTree(World world, String generatorOptions) {
		this.world = world;
		switch (world.provider.getDimensionType()) {
			case NETHER:
				genNetherBridge = (MapGenNetherBridge) TerrainGen.getModdedMapGen(new MapGenNetherBridge(),
						InitMapGenEvent.EventType.NETHER_BRIDGE);
				break;
			case THE_END:
				spikes = new WorldGenSpikes();
				endCityGen = new MapGenEndCity(new ChunkGeneratorEnd(world, false, world.getSeed(),
						world.getSpawnPoint()));
				break;
		}
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		Chunk chunk = new Chunk(this.world, x, z);
		switch (world.provider.getDimensionType()) {
			case NETHER:
				genNetherBridge.generate(world, x, z, new ChunkPrimer());
				break;
			case OVERWORLD:
				generatePlatform(chunk, x, z);
				break;
			case THE_END:
				endCityGen.generate(world, x, z, new ChunkPrimer());
				break;
		}
		chunk.generateSkylightMap();
		return chunk;
	}

	private void generatePlatform(Chunk chunk, int x, int z) {
		if (x == 0 && z == 0) {
			// Platform
			BlockPos pos = new BlockPos(0, 63, 0);
			for (int blockY = 0; blockY < 3; blockY++)
				for (int blockX = 0; blockX < 3; blockX++)
					for (int blockZ = 0; blockZ < 3; blockZ++)
						chunk.setBlockState(pos.east(blockX).south(blockZ).down(blockY),
								blockY == 0 ? Blocks.GRASS.getDefaultState() : Blocks.DIRT.getDefaultState());
		}
	}

	@Override
	public void populate(int x, int z) {
		switch (world.provider.getDimensionType()) {
			case NETHER:
				genNetherBridge.generateStructure(world, world.rand, new ChunkPos(x, z));
				break;
			case THE_END:
				// YUNoMakeGoodMap
				endCityGen.generateStructure(world, world.rand, new ChunkPos(x, z));
				WorldGenSpikes.EndSpike[] aworldgenspikes$endspike = BiomeEndDecorator.getSpikesForWorld(world);
				for (WorldGenSpikes.EndSpike worldgenspikes$endspike : aworldgenspikes$endspike)
					if (worldgenspikes$endspike.doesStartInChunk(new BlockPos(x * 16, 0, z * 16))) {
						this.spikes.setSpike(worldgenspikes$endspike);
						this.spikes.generate(world, world.rand, new BlockPos(worldgenspikes$endspike.getCenterX(), 45,
								worldgenspikes$endspike.getCenterZ()));
					}
				if (x == 0 && z == 0)
					world.setBlockState(new BlockPos(0, 45, 0), Blocks.END_STONE.getDefaultState());
				break;
		}
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return true;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		Biome biome = this.world.getBiome(pos);
		return biome.getSpawnableList(creatureType);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return false;
	}
}
