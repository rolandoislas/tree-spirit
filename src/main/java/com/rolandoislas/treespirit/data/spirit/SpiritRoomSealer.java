package com.rolandoislas.treespirit.data.spirit;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Rolando on 3/3/2017.
 */
public class SpiritRoomSealer extends SpiritSapling {
	private boolean sealed;
	private AxisAlignedBB dimensions;

	SpiritRoomSealer(World worldIn, BlockPos pos, String playerUuid) {
		super(worldIn, pos, playerUuid);
	}

	SpiritRoomSealer(World worldIn, BlockPos pos) {
		super(worldIn, pos);
	}

	public void setSealed(boolean sealed) {
		this.sealed = sealed;
	}

	public void setDimensions(AxisAlignedBB dimensions) {
		this.dimensions = dimensions;
	}

	public AxisAlignedBB getDimensions() {
		return dimensions;
	}

	public boolean isSealed() {
		return sealed;
	}
}
