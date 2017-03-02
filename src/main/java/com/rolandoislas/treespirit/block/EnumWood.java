package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.data.SubItem;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Rolando on 2/27/2017.
 */

public enum EnumWood implements IStringSerializable, SubItem {
	NORMAL(0),
	ELDER(1),
	DIMENSION(2);

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
