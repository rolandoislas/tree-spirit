package com.rolandoislas.treespirit;

import com.rolandoislas.treespirit.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

@Mod(modid = TreeSpirit.MODID, version = TreeSpirit.VERSION, name = TreeSpirit.NAME,
        guiFactory = "com.rolandoislas.treespirit.gui.GuiFactory")
public class TreeSpirit {
    public static final String MODID = "treespirit";
    public static final String VERSION = "1.0";
    public static final String NAME = "Tree Spirit";
    @Mod.Instance(MODID)
    public static TreeSpirit instance;
    @SidedProxy(clientSide = "com.rolandoislas.treespirit.proxy.ClientProxy",
            serverSide = "com.rolandoislas.treespirit.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static Logger logger;
    public static SimpleNetworkWrapper networkChannel;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
