package com.rolandoislas.treespirit.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

/**
 * Created by Rolando on 3/3/2017.
 */
public class AddDurabilityRecipe implements IRecipe {
	private final Item returnItem;
	private final Item durabilityIncreaseItem;
	private final float percentIncrease;
	private ResourceLocation regName;

	public AddDurabilityRecipe(Item returnItem, Item durabilityIncreaseItem, float percentIncrease) {
		this.returnItem = returnItem;
		this.durabilityIncreaseItem = durabilityIncreaseItem;
		this.percentIncrease = percentIncrease;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean foundReturnItem = false;
		boolean foundDurabilityIncreaseItem = false;
		for (int item = 0; item < inv.getSizeInventory(); item++) {
			Item slotItem = inv.getStackInSlot(item).getItem();
			if (slotItem != Items.AIR && slotItem != returnItem && slotItem != durabilityIncreaseItem)
				return false;
			if ((slotItem == returnItem && foundReturnItem) ||
					(slotItem == durabilityIncreaseItem && foundDurabilityIncreaseItem))
				return false;
			if (slotItem == returnItem)
				foundReturnItem = true;
			if (slotItem == durabilityIncreaseItem)
				foundDurabilityIncreaseItem = true;
		}
		return foundReturnItem && foundDurabilityIncreaseItem;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		for (int item = 0; item < inv.getSizeInventory(); item++) {
			ItemStack slotItem = inv.getStackInSlot(item).copy();
			if (slotItem.getItem() == returnItem) {
				int damage = (int) (slotItem.getItemDamage() - slotItem.getMaxDamage() * percentIncrease);
				if (damage < 0)
					damage = 0;
				slotItem.setItemDamage(damage);
				return slotItem;
			}
		}
		return new ItemStack(returnItem);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width <= 2 && height <= 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(returnItem, 1);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	@Override
	public IRecipe setRegistryName(ResourceLocation name) {
		this.regName = name;
		return this;
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return regName;
	}

	@Override
	public Class<IRecipe> getRegistryType() {
		return null;
	}

	public IRecipe setRegistryName(String name) {
		this.setRegistryName(new ResourceLocation(name));
		return this;
	}
}
