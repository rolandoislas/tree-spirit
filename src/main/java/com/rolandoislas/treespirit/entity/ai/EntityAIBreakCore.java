package com.rolandoislas.treespirit.entity.ai;

import com.rolandoislas.treespirit.data.Messages;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.util.SpiritUtil;
import com.rolandoislas.treespirit.util.WorldUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Rolando on 3/2/2017.
 */
public class EntityAIBreakCore extends EntityAIBase {
	private final EntityCreature entity;
	private BlockPos core;
	private int breakProgress = -1;
	private int breakCap = 400;
	private int progressInt = 0;

	public EntityAIBreakCore(EntityCreature entity) {
		this.entity = entity;
		if (entity instanceof EntityCreeper)
			breakCap = 100;
	}

	@Override
	public boolean shouldExecute() {
		BlockPos coreLocal = WorldUtil.getBlockNearby(entity.getPosition(), entity.world,
				1, (int) entity.height, 1, ModBlocks.CORE);
		if (coreLocal == null)
			return false;
		core = coreLocal;
		return entity.world.getBlockState(core).getBlock() == ModBlocks.CORE;
	}

	@Override
	public void startExecuting() {
		breakProgress = 0;
		String playerId = SpiritUtil.getOwnerId(entity.world, core);
		for (EntityPlayer player : entity.world.playerEntities)
			if (EntityPlayer.getUUID(player.getGameProfile()).toString().equals(playerId))
				SpiritUtil.sendMessage(player, Messages.MOB_ATTACKING_CORE, player.getDisplayName(),
						entity.getDisplayName(), entity.world.provider.getDimensionType().getName());
	}

	@Override
	public void resetTask() {
		if (core != null) this.entity.world.sendBlockBreakProgress(entity.getEntityId(), core, -1);
		breakProgress = 0;
	}

	@Override
	public void updateTask() {
		breakProgress++;
		if (entity instanceof EntityCreeper) {
			if (breakProgress >= breakCap)
				((EntityCreeper) entity).ignite();
			return;
		}
		int newProgressInt = (int) ((float) breakProgress / breakCap * 10f);
		this.entity.world.sendBlockBreakProgress(entity.getEntityId(), core, newProgressInt);
		// TODO Spawn arrow for skeleton
		//if (progressInt != newProgressInt && entity instanceof EntitySkeleton)
		// Check if the block should break
		progressInt = newProgressInt;
		if (breakProgress >= breakCap)
			entity.world.setBlockToAir(core);
	}
}
