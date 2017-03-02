package com.rolandoislas.treespirit.event;

import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.util.SpiritUtil;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Rolando on 2/20/2017.
 */
public class EventHandlerCommon {

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		Config.configChanged(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerDeathEvent(LivingDeathEvent event) {
		SpiritUtil.playerDeathEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void tickEvent(TickEvent.PlayerTickEvent event) {
		SpiritUtil.tickEvent(event);
	}

	@SubscribeEvent(priority =  EventPriority.NORMAL)
	public void entitySpawnedEvent(EntityJoinWorldEvent event) {
		SpiritUtil.entitySpawnedEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void entityDropsEvent(LivingDropsEvent event) {
		SpiritUtil.entityDropsEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void blocksDropsEvent(BlockEvent.HarvestDropsEvent event) {
		SpiritUtil.blockDropsEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		SpiritUtil.playerLoggedInEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void worldLoadedEvent(WorldEvent.Load event) {
		SpiritUtil.worldLoaded(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void worldSaveEvent(WorldEvent.Save event) {
		SpiritUtil.worldSaveEvent(event);
	}
}
