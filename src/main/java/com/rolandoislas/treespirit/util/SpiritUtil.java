package com.rolandoislas.treespirit.util;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.data.spirit.SpiritCore;
import com.rolandoislas.treespirit.data.spirit.SpiritData;
import com.rolandoislas.treespirit.data.spirit.SpiritSapling;
import com.rolandoislas.treespirit.data.spirit.SpiritWorldData;
import com.rolandoislas.treespirit.registry.ModBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritUtil {
	private static final DamageSource DAMAGE_TREE_SPIRIT = new DamageSource("tree_spirit")
			.setDamageBypassesArmor().setDamageAllowedInCreativeMode();
	private static HashMap<String, Long> deathTimers = new HashMap<String, Long>();

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
		World world = DimensionManager.getWorld(core.getDimension());
		if (world == null) {
			world = worldIn;
			world.provider.setDimension(core.getDimension());
			ForgeChunkManager.Ticket ticket =
					ForgeChunkManager.requestTicket(TreeSpirit.instance, worldIn, ForgeChunkManager.Type.NORMAL);
			if (ticket == null) {
				TreeSpirit.logger.info(I18n.format(Messages.CHUNK_LOAD_ERROR));
				return;
			}
			TreeSpirit.logger.info(I18n.format(Messages.CHUNK_LOAD_ERROR));
			ForgeChunkManager.forceChunk(ticket, new ChunkPos(pos));
			ForgeChunkManager.releaseTicket(ticket);
		}
		spiritWorldData.removeCore(world, pos);
		JsonUtil.setSpiritData(spiritData);
		deathTimers.remove(core.getPlayerId());
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

	public static void tickEvent(TickEvent.PlayerTickEvent event) {
		if (event.player.world.isRemote)
			return;
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData spiritWorldData = spiritData.getWorld(InfoUtil.getWorldId(event.player.world));
		String playerUuid = InfoUtil.getPlayerUuid(event.player);
		SpiritCore core = spiritWorldData.getCore(playerUuid);
		if (core.getPos() == null)
			return;
		if (!WorldUtil.hasBlockNearby(event.player.getPosition(), event.player.world, 1, 1, 1,
				0, ModBlocks.CORE, ModBlocks.LOG) &&
				!WorldUtil.hasBlockNearby(event.player.getPosition().up(), event.player.world, 1, 1, 1,
						0, ModBlocks.CORE, ModBlocks.LOG) &&
				deathTimers.containsKey(playerUuid)) {
			long time = (System.currentTimeMillis() - deathTimers.get(playerUuid)) / 1000;
			if (time >= Config.deathTime)
				removeCore(event.player.world, core.getPos(), playerUuid);
		}
		else {
			deathTimers.put(playerUuid, System.currentTimeMillis());
		}
	}
}
