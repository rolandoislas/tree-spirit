package com.rolandoislas.touchofbeacon.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Method;

/**
 * Created by Rolando on 3/4/2017.
 */
public class TouchOfBeaconApi {
	private static Method applyFedToPlayersAround = null;
	private static Method applyFedToPlayer = null;
	private static boolean postInit;

	/**
	 * Apply an ambient Fed potion effect to players in a radius.
	 * The effect duration is 5 seconds + 5 per potency level.
	 * @param world world of origin block
	 * @param pos origin block
	 * @param radius radius of player search
	 * @param potency tier of effect to apply
	 */
	public static void applyFedToPlayersAround(World world, BlockPos pos, double radius, int potency) {
		init();
		if (applyFedToPlayersAround == null)
			return;
		try {
			applyFedToPlayersAround.invoke(null, world, pos, radius, potency);
		} catch (Exception ignore) {}
	}

	/**
	 * Apply an ambient Fed potion effect to a player.
	 * The effect duration is 5 seconds + 5 per potency level.
	 * @param player player that will receive the effect
	 * @param potency tier of effect to apply
	 */
	public static void applyFedToPlayer(EntityPlayer player, int potency) {
		init();
		if (applyFedToPlayer == null)
			return;
		try {
			applyFedToPlayer.invoke(null, player, potency);
		} catch (Exception ignore) {}
	}

    /**
     * Call methods via reflection
	 */
	private static void init() {
		if (postInit)
			return;
		try {
			Class<?> foodUtil = Class.forName("com.rolandoislas.touchofbeacon.util.FoodUtil");
			applyFedToPlayer = foodUtil.getMethod("applyFedToPlayer", EntityPlayer.class, int.class);
			applyFedToPlayersAround = foodUtil.getMethod("applyFedToPlayersAround", World.class, BlockPos.class,
					double.class, int.class);
		}
		catch (ClassNotFoundException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		finally {
			postInit = true;
		}
	}
}
