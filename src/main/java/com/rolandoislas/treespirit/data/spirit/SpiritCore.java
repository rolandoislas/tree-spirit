package com.rolandoislas.treespirit.data.spirit;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritCore {
	final Integer dimension;
	final int[] pos;
	private final String playerUid;

	public SpiritCore(World worldIn, BlockPos pos, String playerUuid) {
		this.dimension = worldIn != null ? worldIn.provider.getDimension() : null;
		this.pos = pos != null ? blockPosToArray(pos) : null;
		this.playerUid = playerUuid;
	}

	private int[] blockPosToArray(BlockPos pos) {
		return new int[]{pos.getX(), pos.getY(), pos.getZ()};
	}

	public SpiritCore(World worldIn, BlockPos pos) {
		this(worldIn, pos, "");
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SpiritCore))
			return false;
		if (!playerUid.isEmpty() && playerUid.equals(((SpiritCore) obj).playerUid))
			return true;
		if (dimension != null && pos != null &&
				dimension.equals(((SpiritCore) obj).dimension) &&
				Arrays.equals(pos, ((SpiritCore) obj).pos))
			return true;
		return false;
	}

	public String getPlayerId() {
		return playerUid;
	}

	public Integer getDimension() {
		return dimension;
	}

	@Nullable
	public BlockPos getPos() {
		if (pos == null)
			return null;
		return new BlockPos(pos[0], pos[1], pos[2]);
	}
}
