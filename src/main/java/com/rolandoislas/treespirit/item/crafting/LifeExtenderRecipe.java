package com.rolandoislas.treespirit.item.crafting;

import com.rolandoislas.treespirit.registry.ModItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LifeExtenderRecipe implements IRecipe {
    private final boolean isEmpty;

    public LifeExtenderRecipe(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean ink = false;
        boolean paper = false;
        boolean essence = false;
        int size = 0;
        for (int itemIndex = 0; itemIndex < inv.getSizeInventory(); itemIndex++) {
            ItemStack item = inv.getStackInSlot(itemIndex);
            if (!item.isEmpty())
                size++;
            if (item.isItemEqual(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage())))
                ink = true;
            else if (item.isItemEqual(new ItemStack(ModItems.ESSENCE)))
                essence = true;
            else if (item.isItemEqual(new ItemStack(Items.PAPER)))
                paper = true;
        }
        return ink && paper && essence && size == 3;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack lifeExtender = new ItemStack(ModItems.LIFE_EXTENDER);
        if (isEmpty)
            lifeExtender.setItemDamage(lifeExtender.getMaxDamage());
        return lifeExtender;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.LIFE_EXTENDER);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return null;
    }
}
