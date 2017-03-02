package com.rolandoislas.treespirit.gui.renderer;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

/**
 * Created by Rolando on 3/2/2017.
 */
public class ModItemColors implements IItemColor {
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		return ModBlockColors.GRASS;
	}
}
