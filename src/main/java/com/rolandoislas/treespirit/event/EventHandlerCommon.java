package com.rolandoislas.treespirit.event;

import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.util.SpiritUtil;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
}
