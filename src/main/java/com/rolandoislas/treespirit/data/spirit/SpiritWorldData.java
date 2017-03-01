package com.rolandoislas.treespirit.data.spirit;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritWorldData {
	private ArrayList<SpiritCore> cores = new ArrayList<SpiritCore>();
	private ArrayList<SpiritSapling> saplings = new ArrayList<SpiritSapling>();

	/**
	 * Register a tree core
	 * @param worldIn world containing the block
	 * @param pos tree core position
	 * @param playerUuid player id
	 */
	public void registerCore(World worldIn, BlockPos pos, String playerUuid) {
		SpiritCore core = new SpiritCore(worldIn, pos, playerUuid);
		// Remove old core
		if (cores.contains(core))
			cores.remove(core);
		cores.add(core);
	}

	/**
	 * Register a sapling
	 * @param worldIn world
	 * @param pos sapling position
	 * @param playerUuid player id
	 */
	public void registerSapling(World worldIn, BlockPos pos, String playerUuid) {
		SpiritSapling sapling = new SpiritSapling(worldIn, pos, playerUuid);
		if (saplings.contains(sapling))
			saplings.remove(sapling);
		saplings.add(sapling);
	}

	/**
	 * Removes a sapling
	 * @param worldIn world
	 * @param pos sapling position
	 */
	public void removeSapling(World worldIn, BlockPos pos) {
		saplings.remove(new SpiritSapling(worldIn, pos));
	}

	/**
	 * Get a player UUID string from a sapling
	 *
	 * @param worldIn world
	 * @param pos sapling location
	 * @return player UUID
	 */
	public SpiritSapling getSapling(World worldIn, BlockPos pos) {
		SpiritSapling saplingToFind = new SpiritSapling(worldIn, pos);
		for (SpiritSapling sapling : saplings)
			if (sapling.equals(saplingToFind))
				return sapling;
		return saplingToFind;
	}

	/**
	 * Remove a core
	 * @param worldIn world
	 * @param pos core position
	 */
	public void removeCore(World worldIn, BlockPos pos) {
		cores.remove(new SpiritCore(worldIn, pos));
	}

	/**
	 * Get a player UUID string from a core
	 *
	 * @param worldIn world
	 * @param pos core location
	 * @return player UUID
	 */
	private SpiritCore getCore(World worldIn, BlockPos pos, String playerUuid) {
		SpiritCore coreToFind = new SpiritCore(worldIn, pos, playerUuid);
		for (SpiritCore core : cores)
			if (core.equals(coreToFind))
				return core;
		return coreToFind;
	}

	/**
	 * Get the position from a core
	 * @param playerUuid player id
	 */
	public SpiritCore getCore(String playerUuid) {
		return getCore(null, null, playerUuid);
	}

	public SpiritCore getCore(World worldIn, BlockPos pos) {
		return getCore(worldIn, pos, "");
	}
}
