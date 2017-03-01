package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.block.EnumWood;
import com.rolandoislas.treespirit.data.EnumSubItem;
import com.rolandoislas.treespirit.item.ItemDevTool;
import com.rolandoislas.treespirit.item.ItemEssence;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
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
	public static final Item CORE = createItemBlock(ModBlocks.CORE);

	private static Item createItemBlockWithSubtypes(Block block, final Class<? extends Enum> enumClass,
													final String defaultEnumInstanceName) {
		Item item = new ItemMultiTexture(block, block, new ItemMultiTexture.Mapper() {
			public String apply(ItemStack itemStack) {
				return ((EnumSubItem)Enum.valueOf(enumClass, defaultEnumInstanceName))
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
	}
}
