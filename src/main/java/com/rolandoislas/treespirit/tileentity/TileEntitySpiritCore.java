package com.rolandoislas.treespirit.tileentity;

import com.rolandoislas.touchofbeacon.api.TouchOfBeaconApi;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.data.spirit.EnumPlayerType;
import com.rolandoislas.treespirit.util.InfoUtil;
import com.rolandoislas.treespirit.util.SpiritUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * Created by Rolando on 3/4/2017.
 */
public class TileEntitySpiritCore extends TileEntity implements ITickable {
	@Override
	public void update() {
		if (world.getTotalWorldTime() % 80 == 0) {
			if (Config.coreFeedsPlayerType.equals(EnumPlayerType.NORMAL))
				TouchOfBeaconApi.applyFedToPlayersAround(world, pos, 10, 0);
			else if (Config.coreFeedsPlayerType.equals(EnumPlayerType.TREE_SPIRIT)) {
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();
				AxisAlignedBB axisalignedbb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1).expandXyz(10)
						.addCoord(0, world.getHeight(), 0);
				List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);

				for (EntityPlayer entityplayer : list) {
					if (SpiritUtil.playerHasCore(world, InfoUtil.getPlayerUuid(entityplayer)))
						TouchOfBeaconApi.applyFedToPlayer(entityplayer, 0);
				}
			}
		}
	}
}
