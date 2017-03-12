package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.block.EnumMushroomStructure;
import com.rolandoislas.treespirit.block.EnumWood;
import com.rolandoislas.treespirit.data.SubItem;
import com.rolandoislas.treespirit.gui.renderer.ModItemColors;
import com.rolandoislas.treespirit.item.ItemDevTool;
import com.rolandoislas.treespirit.item.ItemEssence;
import com.rolandoislas.treespirit.item.ItemLifeExtender;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Rolando on 2/23/2017.
 */
public class ModItems {
	public static final Item DEV_TOOL = new ItemDevTool();
	public static final Item ESSENCE = new ItemEssence();
	public static final Item LEAF = createItemBlock(ModBlocks.LEAF);
	public static final Item LOG = createItemBlock(ModBlocks.LOG);
	public static final Item PLANK = createItemBlock(ModBlocks.PLANK);
	public static final Item SAPLING = createItemBlockWithSubtypes(ModBlocks.SAPLING, EnumWood.class,
			EnumWood.NORMAL.name());
	public static final Item CORE = createItemBlockWithSubtypes(ModBlocks.CORE, EnumWood.class,
			EnumWood.NORMAL.name());
	public static final Item GRASS = createItemBlock(ModBlocks.GRASS);
	public static final Item LIFE_EXTENDER = new ItemLifeExtender();
	public static final Item ROOM_SEALER = createItemBlock(ModBlocks.ROOM_SEALER).setMaxStackSize(1);
	public static final Item DOOR = new ItemDoor(ModBlocks.DOOR).setUnlocalizedName(TreeSpirit.MODID + ".door")
			.setRegistryName(TreeSpirit.MODID, "door").setCreativeTab(ModCreativeTabs.MAIN);
	public static final Item MUSHROOM_BUILDER = createItemBlockWithSubtypes(ModBlocks.MUSHROOM_BUILDER,
			EnumMushroomStructure.class, EnumMushroomStructure.MOB_SPAWNER.name()).setMaxStackSize(1);

	private static Item createItemBlockWithSubtypes(Block block, final Class<? extends Enum> enumClass,
													final String defaultEnumInstanceName) {
		Item item = new ItemMultiTexture(block, block, new ItemMultiTexture.Mapper() {
			public String apply(ItemStack itemStack) {
				return ((SubItem)Enum.valueOf(enumClass, defaultEnumInstanceName))
						.getFromMeta(itemStack.getMetadata()).getUnlocalizedName();
			}
		}).setUnlocalizedName(block.getUnlocalizedName())
				.setRegistryName(block.getRegistryName());
		return item;
	}

	private static Item createItemBlock(Block block) {
		return new ItemBlock(block).setUnlocalizedName(block.getUnlocalizedName())
				.setRegistryName(block.getRegistryName()).setCreativeTab(ModCreativeTabs.MAIN)
				.setHasSubtypes(false);
	}

	public static void registerTextures() {
		registerTexture(DEV_TOOL);
		registerTexture(ESSENCE);
		registerTexture(LEAF);
		registerTexture(LOG);
		registerTexture(PLANK);
		registerTexture(SAPLING);
		registerTexture(CORE);
		registerTexture(GRASS);
		registerTexture(LIFE_EXTENDER);
		registerTexture(ROOM_SEALER);
		registerTexture(DOOR);
		registerTexture(MUSHROOM_BUILDER);
	}

	private static void registerTexture(Item item) {
		NonNullList<ItemStack> subItems = NonNullList.create();
		item.getSubItems(item, ModCreativeTabs.MAIN, subItems);
		for (ItemStack subItem : subItems) {
			ResourceLocation registryName = subItem.getItem().getRegistryName();
			String[] split = subItem.getUnlocalizedName().split("\\.");
			String variant = split[split.length - 1];
			if (subItems.size() > 1)
				registryName = new ResourceLocation(registryName.getResourceDomain(),
						registryName.getResourcePath() + "_" + variant);
			ModelLoader.setCustomModelResourceLocation(subItem.getItem(), subItem.getMetadata(),
					new ModelResourceLocation(registryName, "inventory"));
		}
	}

	public static void register() {
		GameRegistry.register(DEV_TOOL);
		GameRegistry.register(ESSENCE);
		GameRegistry.register(LEAF);
		GameRegistry.register(LOG);
		GameRegistry.register(PLANK);
		GameRegistry.register(SAPLING);
		GameRegistry.register(CORE);
		GameRegistry.register(GRASS);
		GameRegistry.register(LIFE_EXTENDER);
		GameRegistry.register(ROOM_SEALER);
		GameRegistry.register(DOOR);
		GameRegistry.register(MUSHROOM_BUILDER);
	}

	public static void registerColors() {
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ModItemColors(), GRASS);
	}
}
