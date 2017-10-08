package com.rolandoislas.treespirit.proxy;

import com.rolandoislas.treespirit.event.EventHandlerClient;
import com.rolandoislas.treespirit.gui.renderer.ModBlockColors;
import com.rolandoislas.treespirit.gui.renderer.ModItemColors;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Rolando on 2/20/2017.
 */
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		ModItems.registerTextures();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		// Register colors
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new ModBlockColors(),
				ModBlocks.GRASS);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ModItemColors(), ModItems.GRASS);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}
