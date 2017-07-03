package com.rolandoislas.treespirit.registry;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.network.MessageCoreCountdown;
import com.rolandoislas.treespirit.network.MessageHandlerCoreCountdown;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Rolando on 3/8/2017.
 */
public class Network {
	public static void register() {
		TreeSpirit.networkChannel = NetworkRegistry.INSTANCE.newSimpleChannel(TreeSpirit.MODID + "y");
		TreeSpirit.networkChannel.registerMessage(MessageHandlerCoreCountdown.class,
				MessageCoreCountdown.class, 0, Side.CLIENT);
	}
}
