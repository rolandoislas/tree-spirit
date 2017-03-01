package com.rolandoislas.treespirit.gui;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.Config;
import net.minecraft.client.gui.GuiScreen;

/**
 * Created by Rolando on 2/21/2017.
 */
public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig {
	public GuiConfig(GuiScreen parentScreen) {
		super(parentScreen,
				Config.getCategories(),
				TreeSpirit.MODID,
				false,
				false,
				TreeSpirit.NAME + " Config");
		titleLine2 = Config.getConfig().getConfigFile().getAbsolutePath();
	}
}
