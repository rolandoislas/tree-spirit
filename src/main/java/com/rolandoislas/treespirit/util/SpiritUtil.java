package com.rolandoislas.treespirit.util;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.block.EnumWood;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.data.spirit.*;
import com.rolandoislas.treespirit.entity.ai.EntityAIBreakCore;
import com.rolandoislas.treespirit.entity.ai.EntityAIMoveToCore;
import com.rolandoislas.treespirit.network.MessageCoreCountdown;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModItems;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritUtil {
	private static final DamageSource DAMAGE_TREE_SPIRIT = new DamageSource("tree_spirit")
			.setDamageBypassesArmor().setDamageAllowedInCreativeMode();
	private static ArrayList<DeathTimer> deathTimers = new ArrayList<DeathTimer>();

	/**
	 * Register a core
	 * @param worldIn world containing core
	 * @param pos core position
	 */
	public static void registerCore(World worldIn, BlockPos pos) {
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorld = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		SpiritSapling sapling = spiritWorld.getSapling(worldIn, pos);
		if (sapling.getPlayerId().isEmpty())
			return;
		spiritWorld.registerCore(worldIn, pos, sapling.getPlayerId());
		JsonUtil.setSpiritData(spiritData);
		EntityPlayer player = WorldUtil.getPlayer(sapling.getPlayerId());
		if (player != null)
			sendMessage(player, Messages.CORE_REGISTERED);
	}

	/**
	 * Send a translated message
	 * @param player player that will get teh message
	 * @param translationKey lang key
	 * @param args formatting objects
	 */
	public static void sendMessage(EntityPlayer player, String translationKey, Object... args) {
		player.sendMessage(new TextComponentTranslation(translationKey, args));
	}

	/**
	 * Add a sapling
	 * @param worldIn world of sapling
	 * @param pos sapling position
	 * @param player player that placed it
	 */
	public static void registerSapling(World worldIn, BlockPos pos, EntityPlayer player) {
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorldData = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		spiritWorldData.registerSapling(worldIn, pos, InfoUtil.getPlayerUuid(player));
		JsonUtil.setSpiritData(spiritData);
	}

	/**
	 * Remove a sapling
	 * @param worldIn world of sapling
	 * @param pos sapling position
	 */
	public static void removeSapling(World worldIn, BlockPos pos) {
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorldData = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		spiritWorldData.removeSapling(worldIn, pos);
		JsonUtil.setSpiritData(spiritData);
	}

	/**
	 * Remove a core and kill the linked player
	 * @param worldIn world containing core
	 * @param pos core position
	 * @param playerUuid
	 */
	public static void removeCore(World worldIn, BlockPos pos, String playerUuid) {
		if (worldIn.isRemote)
			return;
		// Get data
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorldData = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		SpiritCore core = spiritWorldData.getCore(playerUuid);
		if (core.getDimension() == null)
			core = spiritWorldData.getCore(worldIn, pos);
		if (core.getDimension() == null || core.getPos() == null)
			return;
		EntityPlayer player = WorldUtil.getPlayer(core.getPlayerId());
		if (player != null)
			player.attackEntityFrom(DAMAGE_TREE_SPIRIT, player.getMaxHealth());
		// Reset
		World world = WorldUtil.getWorldWithLoadedChunk(worldIn, pos, core.getDimension());
		if (world == null)
			return;
		spiritWorldData.removeCore(world, pos);
		JsonUtil.setSpiritData(spiritData);
		if (player != null)
			TreeSpirit.networkChannel.sendTo(new MessageCoreCountdown(), (EntityPlayerMP) player);
		for (DeathTimer deathTimer : deathTimers) {
			if (deathTimer.equals(core.getPlayerId())) {
				deathTimers.remove(deathTimer);
				break;
			}
		}
		for (SpiritDimensionCore dimensionCore : spiritWorldData.getDimensionCores(core.getPlayerId()))
			removeDimensionCore(worldIn, dimensionCore.getPos(), dimensionCore.getPlayerId());
		if (world.getBlockState(pos).getBlock() == ModBlocks.CORE)
			world.setBlockToAir(pos);
	}

	/**
	 * Handle player deaths of players linked to a core
	 * @param event death event
	 */
	public static void playerDeathEvent(LivingDeathEvent event) {
		if (!(event.getEntity() instanceof EntityPlayer))
			return;
		if (event.getSource() == SpiritUtil.DAMAGE_TREE_SPIRIT)
			return;
		final EntityPlayer player = (EntityPlayer) event.getEntity();
		if (player.world.isRemote)
			return;
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorldData = spiritData.getWorld(InfoUtil.getWorldId(player.world));
		SpiritCore core = spiritWorldData.getCore(InfoUtil.getPlayerUuid(player));
		if (core.getPos() == null)
			return;
		player.world.provider.setDimension(core.getDimension());
		// Mimic server death message
		for (EntityPlayer entityPlayer : player.world.playerEntities)
			sendMessage(entityPlayer, Messages.PLAYER_DIED, player.getDisplayName());
		// Remove the core
		removeCore(player.world, core.getPos(), "");
	}

	/**
	 * Check player range from core/ acceptable blocks
	 * @param event forge player tick event
	 */
	public static void tickEvent(TickEvent.PlayerTickEvent event) {
		if (event.side.equals(Side.CLIENT) || event.player.world.isRemote)
			return;
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorldData = spiritData.getWorld(InfoUtil.getWorldId(event.player.world));
		String playerUuid = InfoUtil.getPlayerUuid(event.player);
		SpiritCore core = spiritWorldData.getCore(playerUuid);
		if (core.getPos() == null)
			return;
		DeathTimer deathTimer = null;
		for (DeathTimer timer : deathTimers)
			if (timer.equals(playerUuid))
				deathTimer = timer;
		if (deathTimer == null) {
			deathTimer = new DeathTimer(playerUuid);
			deathTimers.add(deathTimer);
		}
		//deathTimer.update();
		if (!WorldUtil.hasBlocksNearbyViaChain(event.player.getPosition(), event.player.world, 1, 1, 1,
				Integer.MAX_VALUE, ModBlocks.LOG, ModBlocks.CORE) &&
				!WorldUtil.hasBlocksNearbyViaChain(event.player.getPosition().up(), event.player.world, 1, 1, 1,
						Integer.MAX_VALUE, ModBlocks.LOG, ModBlocks.CORE)) {
			// Damage life extender instead of updating player death counter
			ItemStack offHandItem = event.player.inventory.offHandInventory.get(0);
			if (offHandItem.getItem() == ModItems.LIFE_EXTENDER &&
					offHandItem.getItemDamage() < offHandItem.getMaxDamage()) {
				offHandItem.getItem().setDamage(offHandItem, offHandItem.getItemDamage() + 1);
				if (deathTimer.shouldSendStopMessage())
					TreeSpirit.networkChannel.sendTo(new MessageCoreCountdown(), (EntityPlayerMP) event.player);
			}
			else {
				deathTimer.update();
				if (deathTimer.shouldSendStartMessage())
					TreeSpirit.networkChannel.sendTo(new MessageCoreCountdown(Config.deathTime, deathTimer.getTime()),
							(EntityPlayerMP) event.player);
			}
			// Check death
			if (deathTimer.isDead())
				removeCore(event.player.world, core.getPos(), playerUuid);
		}
		else {
			if (deathTimer.shouldSendStopMessage())
				TreeSpirit.networkChannel.sendTo(new MessageCoreCountdown(), (EntityPlayerMP) event.player);
			deathTimer.reset();
		}
	}

	/**
	 * Get the player id from a block
	 * @param worldIn world of block
	 * @param pos position of block
	 * @return player id or enpty string
	 */
	public static String getOwnerId(World worldIn, BlockPos pos) {
		SpiritWorldData world = JsonUtil.getSpiritData().getWorld(InfoUtil.getWorldId(worldIn));
		String sapling = world.getSapling(worldIn, pos)
				.getPlayerId();
		if (!sapling.isEmpty())
			return sapling;
		String dimensionCore = world.getDimensionCore(worldIn, pos).getPlayerId();
		if (!dimensionCore.isEmpty())
			return dimensionCore;
		return world.getCore(worldIn, pos).getPlayerId();
	}

	/**
	 * Check if any cores are associated with the player id
	 * @param worldIn world of core
	 * @param owner player id
	 * @return does that player have a core
	 */
	public static boolean playerHasCore(World worldIn, String owner) {
		SpiritCore core = JsonUtil.getSpiritData().getWorld(InfoUtil.getWorldId(worldIn)).getCore(owner);
		return (core.getDimension() != null) && (core.getPos() != null);
	}

	/**
	 * Add a dimension core for the player
	 * @param worldIn world of core
	 * @param pos position of core
	 */
	public static void registerDimensionCore(World worldIn, BlockPos pos) {
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorld = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		SpiritSapling sapling = spiritWorld.getSapling(worldIn, pos);
		if (sapling.getPlayerId().isEmpty())
			return;
		spiritWorld.registerDimensionCore(worldIn, pos, sapling.getPlayerId());
		JsonUtil.setSpiritData(spiritData);
		EntityPlayer player = WorldUtil.getPlayer(sapling.getPlayerId());
		if (player != null)
			sendMessage(player, Messages.DIMENSION_CORE_REGISTERED);
	}

	/**
	 * Check if the player has a dimension core
	 * @param worldIn world of core
	 * @param owner owner
	 * @return do they have a dimension core
	 */
	public static boolean playerHasDimensionCore(World worldIn, String owner) {
		SpiritDimensionCore[] cores = JsonUtil.getSpiritData().getWorld(InfoUtil.getWorldId(worldIn))
				.getDimensionCores(owner);
		return cores.length > 0;
	}

	/**
	 * Check if player has a dimension core in the current dimension
	 * @param worldIn world of core
	 * @param owner player id
	 * @return if player has a dimension core in the passed worlds dimension
	 */
	public static boolean playerHasDimensionCoreInCurrentDimension(World worldIn, String owner) {
		SpiritDimensionCore[] cores = JsonUtil.getSpiritData().getWorld(InfoUtil.getWorldId(worldIn))
				.getDimensionCores(owner);
		for (SpiritDimensionCore core : cores)
			if (core.getDimension() == worldIn.provider.getDimension())
				return true;
		return false;
	}

	/**
	 * Removed a dimensional core from the registry and world
	 * @param worldIn world of core
	 * @param pos position of core
	 * @param playerId player id
	 */
	public static void removeDimensionCore(World worldIn, BlockPos pos, String playerId) {
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorld = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		SpiritDimensionCore core = spiritWorld.getDimensionCore(worldIn, pos);
		World world = WorldUtil.getWorldWithLoadedChunk(worldIn, pos, core.getDimension());
		if (world == null)
			return;
		if (playerId.isEmpty())
			spiritWorld.removeDimensionCore(world, pos);
		else
			spiritWorld.registerDimensionCores(playerId);
		JsonUtil.setSpiritData(spiritData);
		if (world.getBlockState(pos).getBlock() == ModBlocks.CORE)
			world.setBlockToAir(pos);
	}

	/**
	 * Add custom AIa to mobs
	 * @param event event with the entity
	 */
	public static void entitySpawnedEvent(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote)
			return;
		if (event.getEntity() instanceof EntityZombie && Config.zombiesDestroyCore)
			addCoreKillToEntityTasks((EntityCreature) event.getEntity());
		else if (event.getEntity() instanceof EntitySkeleton && Config.skeletonsDestroyCore)
			addCoreKillToEntityTasks((EntityCreature) event.getEntity());
		else if (event.getEntity() instanceof EntityCreeper && Config.creepersDestroyCore)
			addCoreKillToEntityTasks((EntityCreature) event.getEntity());
	}

	/**
	 * Adds Ai to the entity that will destroy nearby cores
	 * @param entity entity that will have the Ai added to it
	 */
	private static void addCoreKillToEntityTasks(EntityCreature entity) {
		entity.targetTasks.addTask(2, new EntityAIMoveToCore(entity, 1, 10));
		entity.tasks.addTask(1, new EntityAIBreakCore(entity));
	}

	/**
	 * Add drops to entities
	 * @param event entity death
	 */
	public static void entityDropsEvent(LivingDropsEvent event) {
		if (event.getEntity().world.isRemote)
			return;
		int chance = 10;
		if (!(event.getSource().getEntity() instanceof EntityPlayer))
			chance = 100;
		chance -= event.getLootingLevel() * chance / 10;
		if (event.getEntity() instanceof EntityMob && event.getEntity().world.rand.nextInt(chance) == 0) {
			for (int drops = event.getEntity().world.rand.nextInt(3) + 1; drops > 0; drops--) {
				EntityItem entityItem = new EntityItem(event.getEntity().world,
						event.getEntity().posX,
						event.getEntity().posY,
						event.getEntity().posZ);
				entityItem.setEntityItemStack(ModItems.ESSENCE.getDefaultInstance());
				event.getDrops().add(entityItem);
			}
		}
	}

	/**
	 * Add drops to blocks
	 * @param event harvest event
	 */
	public static void blockDropsEvent(BlockEvent.HarvestDropsEvent event) {
		if (event.getWorld().isRemote)
			return;
		int chance = 20 - event.getFortuneLevel();
		if ((event.getState().getBlock() == ModBlocks.LEAF || event.getState().getBlock() == Blocks.LEAVES ||
				event.getState().getBlock() == Blocks.LEAVES2) &&
				event.getWorld().rand.nextInt(chance) == 0)
			event.getDrops().add(ModItems.ESSENCE.getDefaultInstance());
	}

	/**
	 * Handle fist login item
	 * @param event log in event
	 */
	public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player.world.isRemote)
			return;
		// Send death timer
		String playerId = InfoUtil.getPlayerUuid(event.player);
		for (DeathTimer deathTimer : deathTimers) {
			if (deathTimer.equals(playerId)) {
				ItemStack offHandItem = event.player.inventory.offHandInventory.get(0);
				if (deathTimer.getTime() > 0 && offHandItem.getItem() == ModItems.LIFE_EXTENDER &&
						offHandItem.getItemDamage() < offHandItem.getMaxDamage())
					TreeSpirit.networkChannel.sendTo(new MessageCoreCountdown(Config.deathTime, deathTimer.getTime()),
							(EntityPlayerMP) event.player);
				else
					TreeSpirit.networkChannel.sendTo(new MessageCoreCountdown(), (EntityPlayerMP) event.player);
			}
		}
		// Give sapling
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData world = spiritData.getWorld(InfoUtil.getWorldId(event.player.world));
		SpiritPlayerInfo playerInfo = world.getPlayerInfo(InfoUtil.getPlayerUuid(event.player));
		if (playerInfo != null)
			return;
		world.registerPlayerInfo(InfoUtil.getPlayerUuid(event.player));
		JsonUtil.setSpiritData(spiritData);
		event.player.inventory.addItemStackToInventory(new ItemStack(ModItems.SAPLING, 1,
				EnumWood.ELDER.getMeta()));
	}

	/**
	 * Initialize on world load
	 * @param event world load
	 */
	public static void worldLoaded(WorldEvent.Load event) {
		if (event.getWorld().isRemote)
			return;
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData worldData = spiritData.getWorld(InfoUtil.getWorldId(event.getWorld()));
		deathTimers = worldData.getDeathTimers();
	}

	/**
	 * Save data on world save
	 * @param event world save event
	 */
	public static void worldSaveEvent(WorldEvent.Save event) {
		if (event.getWorld().isRemote)
			return;
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData worldData = spiritData.getWorld(InfoUtil.getWorldId(event.getWorld()));
		worldData.setDeathTimers(deathTimers);
		JsonUtil.setSpiritData(spiritData);
	}
}
