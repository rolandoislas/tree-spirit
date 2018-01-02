package com.rolandoislas.treespirit.item;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Rolando on 3/3/2017.
 */
public class ItemLifeExtender extends Item {
	public static final int TICKS_PER_SECOND = 40; // 40 because of forge player event

	public ItemLifeExtender() {
		this.setUnlocalizedName(TreeSpirit.MODID + ".life_extender");
		this.setRegistryName(TreeSpirit.MODID, "life_extender");
		this.setCreativeTab(ModCreativeTabs.MAIN);
		this.setMaxDamage(TICKS_PER_SECOND * Config.lifeExtenderMaxSeconds);
		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String prefix = getUnlocalizedName(stack);
		tooltip.add(I18n.format(prefix + ".lore"));
		int totalSeconds = (stack.getMaxDamage() - stack.getItemDamage()) / TICKS_PER_SECOND;
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;
		tooltip.add(I18n.format(prefix + ".time", minutes, seconds));
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}
}
