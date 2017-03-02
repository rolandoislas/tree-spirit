package com.rolandoislas.treespirit.entity.ai;

import com.rolandoislas.treespirit.registry.ModBlocks;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Rolando on 3/2/2017.
 */
public class EntityAIMoveToCore extends EntityAIMoveToBlock {
	public EntityAIMoveToCore(EntityCreature creature, double speedIn, int length) {
		super(creature, speedIn, length);
	}

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).getBlock() == ModBlocks.CORE;
	}
}
