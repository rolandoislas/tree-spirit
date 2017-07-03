package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.block.*;
import com.rolandoislas.treespirit.gui.renderer.ModBlockColors;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
		ForgeRegistries.BLOCKS.register(SAPLING);
		ForgeRegistries.BLOCKS.register(LOG);
		ForgeRegistries.BLOCKS.register(LEAF);
		ForgeRegistries.BLOCKS.register(PLANK);
		ForgeRegistries.BLOCKS.register(CORE);
		ForgeRegistries.BLOCKS.register(GRASS);
		ForgeRegistries.BLOCKS.register(ROOM_SEALER);
		ForgeRegistries.BLOCKS.register(DOOR);
		ForgeRegistries.BLOCKS.register(MUSHROOM_BUILDER);
		ForgeRegistries.BLOCKS.register(AIR);
	}

	public static void registerColors() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ModBlockColors(),
				GRASS);
	}
}
