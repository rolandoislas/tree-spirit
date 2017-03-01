package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.block.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Rolando on 2/27/2017.
 */
public class ModBlocks {
	public static Block SAPLING = new BlockSpiritSapling();
	public static Block LOG = new BlockSpiritLog();
	public static Block LEAF = new BlockSpiritLeaf();
	public static Block PLANK = new BlockSpiritPlank();
	public static Block CORE = new BlockSpiritCore();

	public static void register() {
		GameRegistry.register(SAPLING);
		GameRegistry.register(LOG);
		GameRegistry.register(LEAF);
		GameRegistry.register(PLANK);
		GameRegistry.register(CORE);
	}
}
