package com.rolandoislas.treespirit.data.spirit;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritCore {
	private final Integer dimension;
	private final int[] pos;
	private final String playerUid;
	private int level;

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
		return equals(this, obj, true);
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

	/**
	 * Check if objects match
	 * @param core core instance
	 * @param obj object to check against
	 * @param checkId should check id
	 *                If the id is not check equality will be determined by the dimension and position
	 * @return matches
	 */
	public static boolean equals(SpiritCore core, Object obj, boolean checkId) {
		if (!(obj instanceof SpiritCore))
			return false;
		// Check id matches (first)
		if (checkId && !core.playerUid.isEmpty() && core.playerUid.equals(((SpiritCore) obj).playerUid))
			return true;
		// Check dimension and position match
		if (core.dimension != null && core.pos != null &&
				core.dimension.equals(((SpiritCore) obj).dimension) &&
				Arrays.equals(core.pos, ((SpiritCore) obj).pos))
			return true;
		return false;
	}

	public boolean isEmpty() {
		return getDimension() == null || getPos() == null || getPlayerId() == null;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
