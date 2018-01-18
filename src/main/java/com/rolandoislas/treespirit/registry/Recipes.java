package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.block.EnumWood;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.item.ItemLifeExtender;
import com.rolandoislas.treespirit.item.crafting.AddDurabilityRecipe;
import com.rolandoislas.treespirit.item.crafting.LifeExtenderRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by Rolando on 2/25/2017.
 */
public class Recipes {
	public static void register() {
		ResourceLocation name = new ResourceLocation(TreeSpirit.MODID);
		// Planks
		ForgeRegistries.RECIPES.register(
				new ShapelessOreRecipe(name, new ItemStack(ModItems.PLANK, 4), "treeSpiritLog")
						.setRegistryName("planks"));
		// Normal sapling
		ForgeRegistries.RECIPES.register(
				new ShapelessOreRecipe(name, ModItems.SAPLING, "treeSapling", "treeSpiritEssence")
						.setRegistryName("sapling"));
		// Elder sapling
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(name,
				new ItemStack(ModItems.SAPLING, 1, EnumWood.ELDER.getMeta()),
				"treeSpiritSaplingNormal", "treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence",
				"treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence",
				"treeSpiritEssence")
				.setRegistryName("sapling.elder"));
		// Dimension Sapling
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(name,
				new ItemStack(ModItems.SAPLING, 1, EnumWood.DIMENSION.getMeta()),
				"treeSpiritSaplingNormal",
				"treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence",
				Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_PEARL)
				.setRegistryName("sapling.dimension"));
		// Grass
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(name, ModItems.GRASS, "dirt", "treeSpiritEssence",
				"treeSpiritEssence", "treeSpiritEssence", "treeSpiritEssence")
				.setRegistryName("grass...tastes.bad"));
		ForgeRegistries.RECIPES.register(
				new ShapelessOreRecipe(name, ModItems.GRASS, Blocks.GRASS, "treeSpiritEssence")
						.setRegistryName("grass"));
		// Life Extender
		ForgeRegistries.RECIPES.register(
				new AddDurabilityRecipe(ModItems.LIFE_EXTENDER, ModItems.ESSENCE, .1f)
						.setRegistryName("life.extender.repair"));
		ForgeRegistries.RECIPES.register(new LifeExtenderRecipe(true)
				.setRegistryName("life.extender"));
		// Room Sealer
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(name, ModItems.ROOM_SEALER, Blocks.BROWN_MUSHROOM,
				ModItems.ESSENCE)
				.setRegistryName("room.sealer"));
		// Door
		ForgeRegistries.RECIPES.register(
				new ShapedOreRecipe(name, ModItems.DOOR, "WW ", "WW ", "WW ", 'W', ModBlocks.PLANK)
						.setRegistryName("door"));
		ForgeRegistries.RECIPES.register(
				new ShapedOreRecipe(name, ModItems.DOOR, " WW", " WW", " WW", 'W', ModBlocks.PLANK)
						.setRegistryName("door.mirror"));
		// Core
		ForgeRegistries.RECIPES.register(new ShapedOreRecipe(name, new ItemStack(ModItems.CORE, 1, 0),
				"EEE", "EWE", "EEE", 'E', ModItems.ESSENCE, 'W', ModBlocks.LOG)
				.setRegistryName("core"));
		// Mushroom Builder
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(name, new ItemStack(ModItems.MUSHROOM_BUILDER, 1, 0),
				ModItems.ROOM_SEALER, Items.WOODEN_SWORD)
				.setRegistryName("mushroom.builder.mob.spawner"));
	}
}
