package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.tileentity.TileEntitySpiritCore;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Rolando on 2/27/2017.
 */
public class TileEntities {
	public static void register() {
		GameRegistry.registerTileEntity(TileEntitySpiritCore.class, TreeSpirit.MODID + ".spiritcore");
	}
}
