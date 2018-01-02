package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.block.EnumWood;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.item.ItemLifeExtender;
import com.rolandoislas.treespirit.item.crafting.AddDurabilityRecipe;
import com.rolandoislas.treespirit.item.crafting.LifeExtenderRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by Rolando on 2/25/2017.
 */
public class Recipes {
	public static void register() {
		// Planks
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.PLANK, 4), "treeSpiritLog"));
		// Normal sapling
		GameRegistry.addRecipe(new ShapelessOreRecipe(ModItems.SAPLING, "treeSapling", "treeSpiritEssence"));
		// Elder sapling
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(ModItems.SAPLING, 1, EnumWood.ELDER.getMeta()),
				"treeSpiritSaplingNormal", "treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence",
				"treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence",
				"treeSpiritEssence"));
		// Dimension Sapling
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				new ItemStack(ModItems.SAPLING, 1, EnumWood.DIMENSION.getMeta()),
				"treeSpiritSaplingNormal",
				"treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence",
				Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL));
		// Grass
		GameRegistry.addRecipe(new ShapelessOreRecipe(ModItems.GRASS, "dirt", "treeSpiritEssence",
				"treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(ModItems.GRASS, Blocks.GRASS, "treeSpiritEssence"));
		// Life Extender
		GameRegistry.addRecipe(new AddDurabilityRecipe(ModItems.LIFE_EXTENDER, ModItems.ESSENCE, .1f));
		GameRegistry.addRecipe(new LifeExtenderRecipe(true));
		// Room Sealer
		GameRegistry.addRecipe(new ShapelessOreRecipe(ModItems.ROOM_SEALER, Blocks.BROWN_MUSHROOM,
				ModItems.ESSENCE));
		// Door
		GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.DOOR, "WW ", "WW ", "WW ", 'W', ModBlocks.PLANK));
		GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.DOOR, " WW", " WW", " WW", 'W', ModBlocks.PLANK));
		// Core
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.CORE, 1, 0),
				"EEE", "EWE", "EEE", 'E', ModItems.ESSENCE, 'W', ModBlocks.LOG));
		// Mushroom Builder
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.MUSHROOM_BUILDER, 1, 0),
				ModItems.ROOM_SEALER, Items.WOODEN_SWORD));
	}
}
