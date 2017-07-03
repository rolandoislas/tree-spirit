package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.data.SubItem;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Rolando on 3/11/2017.
 */
public enum EnumMushroomStructure implements SubItem, IStringSerializable {
	MOB_SPAWNER(0);

	private final int meta;

	EnumMushroomStructure(int meta) {
		this.meta = meta;
	}

	@Override
	public EnumMushroomStructure getFromMeta(int meta) {
		for (EnumMushroomStructure type : values())
			if (type.getMeta() == meta)
				return type;
		return MOB_SPAWNER;
	}

	@Override
	public int getMeta() {
		return meta;
	}

	@Override
	public String getUnlocalizedName() {
		return this.name().toLowerCase();
	}

	@Override
	public String getName() {
		return this.getUnlocalizedName();
	}
}
