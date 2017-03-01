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
		SpiritData spiritData = JsonUtil.getSpiritData();
		SpiritWorldData worldData = spiritData.getWorld(InfoUtil.getWorldId(worldIn));
		String playerId = "";
		if (worldIn.getBlockState(pos).getBlock() == ModBlocks.SAPLING) {
			SpiritSapling sapling = worldData.getSapling(worldIn, pos);
			playerId = sapling.getPlayerId();
		}
		if (worldIn.getBlockState(pos).getBlock() == ModBlocks.CORE) {
			SpiritCore core = worldData.getCore(worldIn, pos);
			playerId = core.getPlayerId();
		}
		EntityPlayer owner = WorldUtil.getPlayer(playerId);
		if (owner == null)
			return EnumActionResult.FAIL;
		player.sendMessage(new TextComponentString("Owner: " + owner.getDisplayNameString()));
		return EnumActionResult.SUCCESS;
	}
}
