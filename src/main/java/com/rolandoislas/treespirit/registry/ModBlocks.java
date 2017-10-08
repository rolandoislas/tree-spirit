package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.block.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Rolando on 2/27/2017.
 */
public class ModBlocks {
	public static final Block GRASS = new BlockSpiritGrass();
	public static final Block SAPLING = new BlockSpiritSapling();
	public static final Block LOG = new BlockSpiritLog();
	public static final Block LEAF = new BlockSpiritLeaf();
	public static final Block PLANK = new BlockSpiritPlank();
	public static final Block CORE = new BlockSpiritCore();
	public static final Block ROOM_SEALER = new BlockRoomSealer();
	public static final Block DOOR = new BlockSpiritDoor();
	public static final Block MUSHROOM_BUILDER = new BlockMushroomBuilder();
	public static final Block AIR = new BlockSpiritAir();

	public static void register() {
		GameRegistry.register(SAPLING);
		GameRegistry.register(LOG);
		GameRegistry.register(LEAF);
		GameRegistry.register(PLANK);
		GameRegistry.register(CORE);
		GameRegistry.register(GRASS);
		GameRegistry.register(ROOM_SEALER);
		GameRegistry.register(DOOR);
		GameRegistry.register(MUSHROOM_BUILDER);
		GameRegistry.register(AIR);
	}
}
