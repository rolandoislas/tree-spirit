package com.rolandoislas.treespirit.proxy;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.event.ChunkLoadingCallback;
import com.rolandoislas.treespirit.event.EventHandlerCommon;
import com.rolandoislas.treespirit.network.MessageCoreCountdown;
import com.rolandoislas.treespirit.network.MessageHandlerCoreCountdown;
import com.rolandoislas.treespirit.registry.*;
import com.rolandoislas.treespirit.util.JsonUtil;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

/**
 * Created by Rolando on 2/20/2017.
 */
public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		Config.setConfigFile(event.getSuggestedConfigurationFile());
		Config.load();
		JsonUtil.setFile(new File(Config.getConfig().getConfigFile().getAbsolutePath().replace("cfg", "json")));
		MinecraftForge.EVENT_BUS.register(new EventHandlerCommon());
		ModBlocks.register();
		ModItems.register();
	}

	public void init(FMLInitializationEvent event) {
		ModOreDictionary.register();
		Recipes.register();
		TileEntities.register();
		ForgeChunkManager.setForcedChunkLoadingCallback(TreeSpirit.instance, new ChunkLoadingCallback());
		TreeSpirit.networkChannel = NetworkRegistry.INSTANCE.newSimpleChannel(TreeSpirit.MODID + "y");
		TreeSpirit.networkChannel.registerMessage(MessageHandlerCoreCountdown.class,
				MessageCoreCountdown.class, 0, Side.CLIENT);

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
}
