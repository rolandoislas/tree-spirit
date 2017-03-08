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
	private ArrayList<SpiritDimensionCore> dimensionCores = new ArrayList<SpiritDimensionCore>();
	private ArrayList<SpiritPlayerInfo> players = new ArrayList<SpiritPlayerInfo>();
	private ArrayList<DeathTimer> deathTimers = new ArrayList<DeathTimer>();
	private ArrayList<SpiritRoomSealer> roomSealers = new ArrayList<SpiritRoomSealer>();

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
	 * Get a core
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
	 * Get the core from a player id
	 * @param playerUuid player id
	 */
	public SpiritCore getCore(String playerUuid) {
		return getCore(null, null, playerUuid);
	}

	/**
	 * Get the core from a block position
	 * @param worldIn world of block
	 * @param pos position of block
	 * @return core
	 */
	public SpiritCore getCore(World worldIn, BlockPos pos) {
		return getCore(worldIn, pos, "");
	}

	/**
	 * Registers a dimension core (will allow multiple cores per world based on config)
	 * @param worldIn world of core
	 * @param pos position of core
	 * @param playerId player id
	 */
	public void registerDimensionCore(World worldIn, BlockPos pos, String playerId) {
		SpiritDimensionCore core = new SpiritDimensionCore(worldIn, pos, playerId);
		if (dimensionCores.contains(core))
			dimensionCores.remove(core);
		dimensionCores.add(core);
	}

	/**
	 * Get a dimension core from a block
	 * @param world world of core
	 * @param pos position of core
	 * @return core
	 */
	public SpiritDimensionCore getDimensionCore(World world, BlockPos pos) {
		SpiritDimensionCore coreToFind = new SpiritDimensionCore(world, pos);
		for (SpiritDimensionCore core : dimensionCores)
			if (core.equals(coreToFind))
				return core;
		return coreToFind;
	}

	/**
	 * Get dimension cores for a player
	 * @param playerId player id
	 * @return dimension cores
	 */
	public SpiritDimensionCore[] getDimensionCores(String playerId) {
		ArrayList<SpiritDimensionCore> dimCores = new ArrayList<SpiritDimensionCore>();
		for (SpiritDimensionCore dimensionCore : dimensionCores)
			if (dimensionCore.getPlayerId().equals(playerId))
				dimCores.add(dimensionCore);
		SpiritDimensionCore[] primitiveCores = new SpiritDimensionCore[dimCores.size()];
		for (SpiritDimensionCore core : dimCores)
			primitiveCores[dimCores.indexOf(core)] = core;
		return primitiveCores;
	}

	/**
	 * Removed a dimension core
	 * @param world world of core
	 * @param pos position of core
	 */
	public void removeDimensionCore(World world, BlockPos pos) {
		dimensionCores.remove(new SpiritDimensionCore(world, pos));
	}

	/**
	 * Remove all the dimension cores for a player
	 * @param playerId player id
	 */
	public void registerDimensionCores(String playerId) {
		for (int core = 0; core < dimensionCores.size(); core++) {
			if (dimensionCores.get(core).getPlayerId().equals(playerId)) {
				dimensionCores.remove(core);
				core--;
			}
		}
	}

	/**
	 * Get player info for player id
	 * @param playerUuid player id
	 * @return player info or null if not found
	 */
	public SpiritPlayerInfo getPlayerInfo(String playerUuid) {
		SpiritPlayerInfo playerToFind = new SpiritPlayerInfo(playerUuid);
		for (SpiritPlayerInfo player : players)
			if (player.equals(playerToFind))
				return player;
		return null;
	}

	/**
	 * Create and add a player info container
	 * @param playerUuid player id
	 */
	public void registerPlayerInfo(String playerUuid) {
		if (getPlayerInfo(playerUuid) != null)
			return;
		players.add(new SpiritPlayerInfo(playerUuid));
	}

	/**
	 * Get death timers
	 * @return death timers
	 */
	public ArrayList<DeathTimer> getDeathTimers() {
		return deathTimers;
	}

	/**
	 * Set death timers
	 * @param deathTimers death timers
	 */
	public void setDeathTimers(ArrayList<DeathTimer> deathTimers) {
		this.deathTimers = deathTimers;
	}

	/**
	 * Register a room sealer
	 * @param worldIn world of sealer
	 * @param pos position of sealer
	 * @param playerUuid player id
	 */
	public void registerRoomSealer(World worldIn, BlockPos pos, String playerUuid) {
		SpiritRoomSealer sealer = new SpiritRoomSealer(worldIn, pos, playerUuid);
		if (roomSealers.contains(sealer))
			roomSealers.remove(sealer);
		roomSealers.add(sealer);
	}

	/**
	 * Get a room sealer from a block pos
	 * @param worldIn world of sealer
	 * @param pos position of sealer
	 * @return room sealer
	 */
	public SpiritRoomSealer getRoomSealer(World worldIn, BlockPos pos) {
		SpiritRoomSealer roomToFind = new SpiritRoomSealer(worldIn, pos);
		for (SpiritRoomSealer roomSealer : roomSealers)
			if (roomSealer.equals(roomToFind))
				return roomSealer;
		return roomToFind;
	}

	/**
	 * Get all room sealers for a player
	 * @param playerUuid player id
	 * @return array of room sealers that can be empty if the player has none registered
	 */
	public SpiritRoomSealer[] getRoomSealers(String playerUuid) {
		ArrayList<SpiritRoomSealer> playerRoomSealers = new ArrayList<SpiritRoomSealer>();
		for (SpiritRoomSealer roomSealer : roomSealers)
			if (roomSealer.getPlayerId().equals(playerUuid))
				playerRoomSealers.add(roomSealer);
		SpiritRoomSealer[] primitive = new SpiritRoomSealer[playerRoomSealers.size()];
		for (SpiritRoomSealer roomSealer : playerRoomSealers)
			primitive[playerRoomSealers.indexOf(roomSealer)] = roomSealer;
		return primitive;
	}

	/**
	 * Unregister a room sealer
	 * @param worldIn world of sealer
	 * @param pos position of sealer
	 */
	public void removeRoomSealer(World worldIn, BlockPos pos) {
		roomSealers.remove(new SpiritRoomSealer(worldIn, pos));
	}
}
