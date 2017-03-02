package com.rolandoislas.treespirit.event;

import com.rolandoislas.treespirit.gui.renderer.CoreCountdownRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Rolando on 2/21/2017.
 */
public class EventHandlerClient {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlayEvent(RenderGameOverlayEvent event) {
		CoreCountdownRenderer.render(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerTickEvent(TickEvent.PlayerTickEvent event) {
		if (event.side.equals(Side.SERVER))
			return;
		CoreCountdownRenderer.playerTickEvent(event);
	}
}
