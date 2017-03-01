package com.rolandoislas.treespirit.data.spirit;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritSapling extends SpiritCore {
	SpiritSapling(World worldIn, BlockPos pos, String playerUuid) {
		super(worldIn, pos, playerUuid);
	}

	SpiritSapling(World worldIn, BlockPos pos) {
		super(worldIn, pos);
	}
}
