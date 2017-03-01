package com.rolandoislas.treespirit.util;

import com.rolandoislas.treespirit.TreeSpirit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Rolando on 2/28/2017.
 */
public class InfoUtil {
	/**
	 * Get the unique world ID.
	 * Uses seed, world name, and directory path.
	 * @return MD5 of world ID
	 */
	public static String getWorldId(World world) {
		if (world.isRemote) {
			TreeSpirit.logger.debug("Tried to get world id for remote world.");
			return "";
		}
		// Construct unique id or use world address if remote
		String id = world.getWorldInfo().getSeed() + world.getWorldInfo().getWorldName() +
				world.getSaveHandler().getWorldDirectory().getAbsolutePath();
		// MD5 because the raw id looks horrible with an escaped path and spaces in it
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(id.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte anArray : array)
				sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
			id = sb.toString();
		} catch (NoSuchAlgorithmException ignore) {}
		return id;
	}

	public static String getPlayerUuid(EntityPlayer player) {
		return EntityPlayer.getUUID(player.getGameProfile()).toString();
	}
}
