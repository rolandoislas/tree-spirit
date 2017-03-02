package com.rolandoislas.treespirit.data.spirit;

import com.rolandoislas.treespirit.data.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Rolando on 3/2/2017.
 */
public class SpiritDimensionCore extends SpiritCore {
	public SpiritDimensionCore(World worldIn, BlockPos pos) {
		super(worldIn, pos);
	}

	public SpiritDimensionCore(World worldIn, BlockPos pos, String playerUuid) {
		super(worldIn, pos, playerUuid);
	}

	@Override
	public boolean equals(Object obj) {
		return SpiritCore.equals(this, obj, false);
	}
}
