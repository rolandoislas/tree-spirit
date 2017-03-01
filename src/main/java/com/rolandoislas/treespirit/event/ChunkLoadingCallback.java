package com.rolandoislas.treespirit.event;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.List;

/**
 * Created by Rolando on 2/28/2017.
 */
public class ChunkLoadingCallback implements ForgeChunkManager.LoadingCallback {
	@Override
	public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {

	}
}
