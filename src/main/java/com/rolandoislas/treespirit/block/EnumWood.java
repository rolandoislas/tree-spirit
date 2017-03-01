package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.data.EnumSubItem;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Rolando on 2/27/2017.
 */

public enum EnumWood implements IStringSerializable, EnumSubItem {
	NORMAL(0),
	ELDER(1);

	private final int meta;

	EnumWood(int meta) {
		this.meta = meta;
	}

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}

	public EnumWood getFromMeta(int meta) {
		for (EnumWood sapling : values())
			if (sapling.getMeta() == meta)
				return sapling;
		return NORMAL;
	}

	public int getMeta() {
		return meta;
	}

	public String getUnlocalizedName() {
		return getName();
	}
}
