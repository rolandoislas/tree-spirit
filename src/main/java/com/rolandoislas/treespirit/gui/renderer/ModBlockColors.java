package com.rolandoislas.treespirit.gui.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

/**
 * Created by Rolando on 3/2/2017.
 */
public class ModBlockColors implements IBlockColor {
	static final int GRASS = hexToInt("186A00");
	private static final String hexadecimal = "0123456789ABCDEF";

	private static int hexToInt(String hexString) {
		double dec = 0;
		for (int hex = 0; hex < hexString.length(); hex++)
			dec += hexadecimal.indexOf(hexString.charAt(hex)) * Math.pow(16, hexString.length() - hex - 1);
		return (int) dec;
	}

	@Override
	public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
		return GRASS;
	}
}
