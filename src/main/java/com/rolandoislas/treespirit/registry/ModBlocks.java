package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.block.*;
import com.rolandoislas.treespirit.gui.renderer.ModBlockColors;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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

	public static void register() {
		GameRegistry.register(SAPLING);
		GameRegistry.register(LOG);
		GameRegistry.register(LEAF);
		GameRegistry.register(PLANK);
		GameRegistry.register(CORE);
		GameRegistry.register(GRASS);
		GameRegistry.register(ROOM_SEALER);
	}

	public static void registerColors() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ModBlockColors(),
				GRASS);
	}
}
