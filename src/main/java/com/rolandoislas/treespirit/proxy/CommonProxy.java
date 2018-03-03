package com.rolandoislas.treespirit.proxy;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import com.rolandoislas.treespirit.event.ChunkLoadingCallback;
import com.rolandoislas.treespirit.event.EventHandlerCommon;
import com.rolandoislas.treespirit.registry.*;
import com.rolandoislas.treespirit.util.JsonUtil;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
		Potions.register();
	}

	public void init(FMLInitializationEvent event) {
		ModOreDictionary.register();
		Recipes.register();
		TileEntities.register();
		ForgeChunkManager.setForcedChunkLoadingCallback(TreeSpirit.instance, new ChunkLoadingCallback());
		Network.register();
		WorldTypes.register();
	}

	public void postInit(FMLPostInitializationEvent event) {
		Config.configChanged(new ConfigChangedEvent.OnConfigChangedEvent(TreeSpirit.MODID, null,
				false, false));
	}
}
