package com.rolandoislas.treespirit.item;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.spirit.SpiritCore;
import com.rolandoislas.treespirit.data.spirit.SpiritData;
import com.rolandoislas.treespirit.data.spirit.SpiritSapling;
import com.rolandoislas.treespirit.data.spirit.SpiritWorldData;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import com.rolandoislas.treespirit.util.InfoUtil;
import com.rolandoislas.treespirit.util.JsonUtil;
import com.rolandoislas.treespirit.util.SpiritUtil;
import com.rolandoislas.treespirit.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Created by Rolando on 2/23/2017.
 */
public class ItemDevTool extends Item {
	public ItemDevTool() {
		this.setUnlocalizedName(TreeSpirit.MODID + ".devtool");
		this.setRegistryName(TreeSpirit.MODID, "devtool");
		this.setCreativeTab(ModCreativeTabs.MAIN);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return EnumActionResult.FAIL;
		String playerId = SpiritUtil.getOwnerId(worldIn, pos);
		String ownerName;
		if (!playerId.isEmpty()) {
			EntityPlayer owner = WorldUtil.getPlayer(playerId);
			if (owner != null)
				ownerName = owner.getDisplayNameString();
			else
				ownerName = "<owner offline>";
		}
		else
			ownerName = "<no owner>";
		player.sendMessage(new TextComponentString("Owner: " + ownerName));
		return EnumActionResult.SUCCESS;
	}
}
