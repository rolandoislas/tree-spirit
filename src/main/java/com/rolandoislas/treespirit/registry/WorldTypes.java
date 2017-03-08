package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.world.WorldProviderEndSkyTree;
import com.rolandoislas.treespirit.world.WorldProviderHellSkyTree;
import com.rolandoislas.treespirit.world.WorldProviderSurfaceSkyTree;
import com.rolandoislas.treespirit.world.WorldTypeSkyTree;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by Rolando on 3/8/2017.
 */
public class WorldTypes {
	public static WorldType skyTree;

	public static void register() {
		skyTree = new WorldTypeSkyTree();
		DimensionManager.unregisterDimension(-1);
		DimensionManager.unregisterDimension(0);
		DimensionManager.unregisterDimension(1);
		DimensionManager.registerDimension(-1, DimensionType.register("Nether", "_nether", -1,
				WorldProviderHellSkyTree.class, false));
		DimensionManager.registerDimension(0,  DimensionType.register("Overworld", "", 0,
				WorldProviderSurfaceSkyTree.class, true));
		DimensionManager.registerDimension(1,  DimensionType.register("The End", "_end", 1,
				WorldProviderEndSkyTree.class, false));
	}
}
